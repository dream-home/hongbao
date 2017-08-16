package com.yanbao.service.impl;

import com.mall.model.*;
import com.yanbao.service.*;
import com.yanbao.util.*;
import com.yanbao.vo.StoreExchangeRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 区域代理商家统计
 * Created by Administrator on 2017/6/19.
 */
@Service
public class InAgentStaticsServiceImpl implements InAgentStaticsService {

    public static Log log = LogFactory.getLog(InAgentStaticsServiceImpl.class);

    @Autowired
    private WalletExchangeService walletExchangeService;
    @Autowired
    private EpRecordService epRecordService;
    @Autowired
    private AgentBillDetailService agentBillDetailService;
    @Autowired
    private AgentBillService agentBillService;
    @Autowired
    private AgentInfoService agentInfoService;
    @Autowired
    private SysCityService sysCityService;
    @Autowired
    private AgentTimeBillService agentTimeBillService;

    /**
     * 统计天结算业绩
     *
     * @return
     * @throws Exception
     */
    @Transactional
    @Override
    public Boolean inAgentStatistics(Date starttime, Date endtime) throws Exception {

        //查询当前时间往前推30天的所有代理业绩
        List<StoreExchangeRecord> storeExchangeRecords = walletExchangeService.getStoreList(starttime, endtime);

        //获取代理商家ep消费记录
        List<StoreExchangeRecord> epList = epRecordService.getEpList(starttime, endtime);

        if ((storeExchangeRecords == null || storeExchangeRecords.size() <= 0) && (epList == null || epList.size() <= 0)) {
            return false;
        }

        StaticVariable.setAll(sysCityService);

        //定义一个Map集合保存代理地区下所有代理
        Map<String, AgentInfo> agentInfoMap = new HashMap<>();
        Map<String,Double> exchangeMap = new HashMap<>();
        Map<String,Double> epMap = new HashMap<>();
        Map<String,AgentInfo> allAgent = new HashMap<>();
        //循环所有提现记录
        for(StoreExchangeRecord exchangeRecord : storeExchangeRecords){
            //根据代理地区id查询上级所有代理
            List<AgentInfo> agentInfoList =null;
            City city = StaticVariable.ALL_CITY_MAP.get(exchangeRecord.getAreaId());
            if(null!=city) {
                List<String> ids = (List<String>) StaticVariable.getCitySetParent(city).get("IDS");
                agentInfoList = agentInfoService.readListByAreaid(ids);
            }
            if(agentInfoList == null){
                agentInfoList = new ArrayList<>();
                AgentInfo model = agentInfoService.getById("100000");
                agentInfoList.add(model);
            }

            //循环插入单笔提现业绩记录
            for (AgentInfo ai : agentInfoList){
                String orderNo = exchangeRecord.getOrderNo() +"_"+ ai.getAgentAreaId();
                createBillDetail(ai,Math.abs(exchangeRecord.getScore()),orderNo,starttime,"1");
                if(ToolUtil.isEmpty(exchangeMap.get(ai.getAgentId()))){
                    exchangeMap.put(ai.getAgentId(),Math.abs(exchangeRecord.getScore()));
                }else{
                    Double exchangeAmount = exchangeMap.get(ai.getAgentId());
                    exchangeAmount = exchangeAmount + Math.abs(exchangeRecord.getScore());
                    exchangeMap.put(ai.getAgentId(),exchangeAmount);
                }
                allAgent.put(ai.getAgentId(),ai);
            }
        }

        //循环所有ep记录，并插入详情表
        for(StoreExchangeRecord epRecord : epList){
            List<AgentInfo> agentInfoList = null;
            //根据代理地区id查询上级所有代理
            City city = StaticVariable.ALL_CITY_MAP.get(epRecord.getAreaId());
            if(null != city) {
                List<String> ids = (List<String>) StaticVariable.getCitySetParent(city).get("IDS");
                agentInfoList = agentInfoService.readListByAreaid(ids);
            }

            if(agentInfoList == null){
                agentInfoList = new ArrayList<>();
                AgentInfo model = agentInfoService.getById("100000");
                agentInfoList.add(model);
            }

            //循环插入单笔提现业绩记录
            for (AgentInfo ai : agentInfoList){
                String orderNo = epRecord.getOrderNo() +"_"+ ai.getAgentAreaId();
                createBillDetail(ai,Math.abs(epRecord.getScore()),orderNo,starttime,"2");
                if(ToolUtil.isEmpty(epMap.get(ai.getAgentId()))){
                    epMap.put(ai.getAgentId(),Math.abs(epRecord.getScore()));
                }else{
                    Double epAmount = epMap.get(ai.getAgentId());
                    epAmount = epAmount + Math.abs(epRecord.getScore());
                    epMap.put(ai.getAgentId(),epAmount);
                }
                allAgent.put(ai.getAgentId(),ai);
            }
        }

        //循环allAgentMap集合,并插入AgentTimeBill记录
        for(String key : allAgent.keySet()){
            AgentInfo agentInfo = allAgent.get(key);
            Double exchangeAmount = ToolUtil.isEmpty(exchangeMap.get(agentInfo.getAgentId())) ? 0d :exchangeMap.get(agentInfo.getAgentId());
            Double epAmount = ToolUtil.isEmpty(epMap.get(agentInfo.getAgentId())) ? 0d :epMap.get(agentInfo.getAgentId());
            createAgentTimeBill(agentInfo,exchangeAmount,epAmount,starttime);

            //叠加当月的所有业绩
            Date monthFirst = DateTimeUtil.getMonthFirst();

            //根据代理id和当月第一天查询代理业绩
            AgentBill agentBill = agentBillService.getAgentBillByTime(agentInfo.getAgentId(),monthFirst);

            createAgentBill(agentInfo,agentBill,exchangeAmount,epAmount,monthFirst);

        }

        return true;
    }


    //统计代理总业绩
    private void createAgentBill(AgentInfo info,AgentBill agentBill,Double exchangeAmount,Double epAmount,Date fromTime) throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Double balanceScale = 0d;
        Double epScale = 0d;
        ParamUtil util = ParamUtil.getIstance();
        //判断代理等级
        if (info.getAgentLevel().intValue() == 1) {
            balanceScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.PROVINCEBALANCESCALE), 0d),100d,4);
            epScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.PROVINCEEPSCALE), 0d),100d,4);
        } else if (info.getAgentLevel().intValue() == 2) {
            balanceScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.CITYBALANCESCALE), 0d),100d,4);
            epScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.CITYEPSCALE), 0d),100d,4);
        } else if (info.getAgentLevel().intValue() == 3) {
            balanceScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.COUNTRYBALANCESCALE), 0d),100d,4);
            epScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.COUNTRYEPSCALE), 0d),100d,4);
        }

        if(agentBill == null){
            //计算结算金额
            Double score = exchangeAmount * balanceScale;
            Double epSore = epAmount * epScale;
            //如果月结算业绩记录为空，那就新增一个代理月结算记录
            agentBill = new AgentBill();
            agentBill.setAgentId(info.getAgentId());
            agentBill.setOrderNo(OrderNoUtil.get());
            agentBill.setCompany(info.getCompany());
            agentBill.setUserName(info.getUserName());
            agentBill.setPhone(info.getPhone());
            agentBill.setBankCard(info.getBankCard());
            agentBill.setBankType(info.getBankType());
            agentBill.setBankTypeCode(info.getBankTypeCode());
            agentBill.setBankBranch(info.getBankBranch());
            agentBill.setAgentAreaId(info.getAgentAreaId());
            agentBill.setAgentProvince(info.getAgentProvince());
            agentBill.setAgentCity(info.getAgentCity());
            agentBill.setAgentCountry(info.getAgentCountry());
            agentBill.setAddress(info.getAddress());
            agentBill.setAgentLevel(info.getAgentLevel());
            agentBill.setBillday(sdf.format(fromTime));
            agentBill.setEP(epAmount);
            agentBill.setEPScale(epScale);
            agentBill.setBalance(exchangeAmount);
            agentBill.setStatus(1);
            agentBill.setBalanceScale(balanceScale);
            agentBill.setTotalAmount(PoundageUtil.getPoundage(score + epSore, 1d));

            agentBillService.createWithUUID(agentBill);
        }else {

            //如果当前月结算记录不为空，则叠加当天提现业绩和ep业绩到当前月结算记录中

            //计算结算金额
            Double scoreDay = agentBill.getBalance() + exchangeAmount;
            Double epDay = agentBill.getEP() + epAmount;
            Double score = scoreDay * balanceScale;
            Double epScore = epDay * epScale;
            Double totalAmount =  PoundageUtil.getPoundage(score + epScore,1d);

            AgentBill model = new AgentBill();
            model.setBalance(scoreDay);
            model.setBalanceScale(balanceScale);
            model.setEP(epDay);
            model.setEPScale(epScale);
            model.setTotalAmount(totalAmount);
            model.setUpdateTime(new Date());

            agentBillService.updateById(agentBill.getId(),model);
        }
    }

    //创建结算单笔业绩记录
    private void createBillDetail(AgentInfo agentInfo,Double money,String orderNo,Date startTime,String type) throws Exception {
        //结算比例
        Double balanceScale = 0d;
        Double epScale = 0d;
        Double scale = 0d;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        AgentBillDetail exchangeBillDetail = new AgentBillDetail();
        //代理等级为空  默认区级
        if(null==agentInfo.getAgentLevel()){
        	agentInfo.setAgentLevel(3);
        }
        ParamUtil util = ParamUtil.getIstance();
        //判断代理等级
        if (agentInfo.getAgentLevel().intValue() == 1) {
            balanceScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.PROVINCEBALANCESCALE), 0d),100d,4);
            epScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.PROVINCEEPSCALE), 0d),100d,4);
        } else if (agentInfo.getAgentLevel().intValue() == 2) {
            balanceScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.CITYBALANCESCALE), 0d),100d,4);
            epScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.CITYEPSCALE), 0d),100d,4);
        } else if (agentInfo.getAgentLevel().intValue() == 3) {
            balanceScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.COUNTRYBALANCESCALE), 0d),100d,4);
            epScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.COUNTRYEPSCALE), 0d),100d,4);
        }

        if("1".equals(type)){
            scale = balanceScale;
            exchangeBillDetail.setRemark("单笔提现业绩");
        }else if("2".equals(type)){
            scale = epScale;
            exchangeBillDetail.setRemark("单笔EP业绩");
        }

        exchangeBillDetail.setAgentId(agentInfo.getAgentId());
        exchangeBillDetail.setType(type);
        exchangeBillDetail.setScale(scale);
        //计算结算金额
        exchangeBillDetail.setAmount(money);
        exchangeBillDetail.setCompany(agentInfo.getCompany());
        exchangeBillDetail.setUserName(agentInfo.getUserName());
        exchangeBillDetail.setPhone(agentInfo.getPhone());
        exchangeBillDetail.setOrderNo(orderNo);
        exchangeBillDetail.setBankCard(agentInfo.getBankCard());
        exchangeBillDetail.setBankType(agentInfo.getBankType());
        exchangeBillDetail.setBankTypeCode(agentInfo.getBankTypeCode());
        exchangeBillDetail.setBankBranch(agentInfo.getBankBranch());
        exchangeBillDetail.setAgentAreaId(agentInfo.getAgentAreaId());
        exchangeBillDetail.setAgentProvince(agentInfo.getAgentProvince());
        exchangeBillDetail.setAgentCity(agentInfo.getAgentCity());
        exchangeBillDetail.setAgentCountry(agentInfo.getAgentCountry());
        exchangeBillDetail.setAddress(agentInfo.getAddress());
        exchangeBillDetail.setAgentLevel(agentInfo.getAgentLevel());
        exchangeBillDetail.setBillday(sdf.format(startTime));
        exchangeBillDetail.setRemark("单笔业绩");
        exchangeBillDetail.setStatus(1);
        exchangeBillDetail.setCreateTime(new Date());
        exchangeBillDetail.setUpdateTime(new Date());

        //新增代理信息结算记录
        agentBillDetailService.createWithUUID(exchangeBillDetail);
    }

    //创建每天所有提现业绩和ep业绩
    private void createAgentTimeBill(AgentInfo agentInfo,Double money,Double ep,Date startTime) throws Exception {
        //结算比例
        Double balanceScale = 0d;
        Double epScale = 0d;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        AgentTimeBill agentTimeBill = new AgentTimeBill();

        ParamUtil util = ParamUtil.getIstance();
        //判断代理等级
        if (agentInfo.getAgentLevel().intValue() == 1) {
            balanceScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.PROVINCEBALANCESCALE), 0d),100d,4);
            epScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.PROVINCEEPSCALE), 0d),100d,4);
        } else if (agentInfo.getAgentLevel().intValue() == 2) {
            balanceScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.CITYBALANCESCALE), 0d),100d,4);
            epScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.CITYEPSCALE), 0d),100d,4);
        } else if (agentInfo.getAgentLevel().intValue() == 3) {
            balanceScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.COUNTRYBALANCESCALE), 0d),100d,4);
            epScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.COUNTRYEPSCALE), 0d),100d,4);
        }
        agentTimeBill.setAgentId(agentInfo.getAgentId());
        agentTimeBill.setCompany(agentInfo.getCompany());
        agentTimeBill.setUserName(agentInfo.getUserName());
        agentTimeBill.setPhone(agentInfo.getPhone());
        agentTimeBill.setOrderNo(OrderNoUtil.get());
        agentTimeBill.setBankCard(agentInfo.getBankCard());
        agentTimeBill.setBankType(agentInfo.getBankType());
        agentTimeBill.setBankTypeCode(agentInfo.getBankTypeCode());
        agentTimeBill.setBankBranch(agentInfo.getBankBranch());
        agentTimeBill.setAgentAreaId(agentInfo.getAgentAreaId());
        agentTimeBill.setAgentLevel(agentInfo.getAgentLevel());
        agentTimeBill.setBillday(1);
        agentTimeBill.setRemark("当天代理业绩");
        agentTimeBill.setStatus(1);
        agentTimeBill.setEP(ep);
        agentTimeBill.setEPScale(epScale);
        agentTimeBill.setBalance(money);
        agentTimeBill.setBalanceScale(balanceScale);
        agentTimeBill.setCreateTime(new Date());
        agentTimeBill.setUpdateTime(new Date());
        agentTimeBill.setTotalAmount(ep + money);

        //新增代理信息结算记录
        agentTimeBillService.createWithUUID(agentTimeBill);
    }

    public static void main(String[] args) {
        System.out.println(Math.abs(-1));
    }

}