package com.yanbao.controller;

import com.mall.model.AgentBill;
import com.mall.model.AgentInfo;
import com.mall.model.City;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.service.AgentBillService;
import com.yanbao.service.AgentInfoService;
import com.yanbao.service.SysCityService;
import com.yanbao.util.DateTimeUtil;
import com.yanbao.util.RandomUtil;
import com.yanbao.util.ToolUtil;
import com.yanbao.vo.AgentBillVo;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/15.
 */
@Controller
@RequestMapping("/agentBill")
public class AgentBillController extends BaseController{

    @Autowired
    private AgentBillService agentBillService;
    @Autowired
    private SysCityService sysCityService;
    @Autowired
    private AgentInfoService agentInfoService;


    /**
     * 根据手机号、姓名的信息精确匹配，公司名称模糊匹配查询代理业绩
     * @param agentBillVo
     * @param page
     * @return
     */
    @RequestMapping("/getAgentPerformance")
    @ResponseBody
    public JsonResult getAgentPerformance(AgentBillVo agentBillVo, Page page) throws Exception{

        AgentBill agentBill = new AgentBill();
        agentBill.setAgentId(agentBillVo.getAgentId());
        if(!ToolUtil.isEmpty(agentBillVo.getPhone())){
            agentBill.setPhone(agentBillVo.getPhone());
        }
        if(!ToolUtil.isEmpty(agentBillVo.getAgentLevel())){
            agentBill.setAgentLevel(agentBillVo.getAgentLevel());
        }
        if(!ToolUtil.isEmpty(agentBillVo.getAgentProvince())){
            agentBill.setAgentProvince(agentBillVo.getAgentProvince());
        }
        if(!ToolUtil.isEmpty(agentBillVo.getAgentCity())){
            agentBill.setAgentCity(agentBillVo.getAgentCity());
        }
        if(!ToolUtil.isEmpty(agentBillVo.getAgentCountry())){
            agentBill.setAgentCountry(agentBillVo.getAgentCountry());
        }
        Page page2=new Page();
        BeanUtils.copyProperties(page2,page);
        if(!ToolUtil.isEmpty(agentBillVo.getStatus())){
            if(agentBillVo.getStatus().intValue() == 3){
                agentBill.setStatus(1);
                page2.setPageNo(1);
                page2.setPageSize(9999);
            }else if(agentBillVo.getStatus().intValue() == 1){
                agentBill.setStatus(1);
                page2.setPageNo(1);
                page2.setPageSize(9999);
            }else{
                agentBill.setStatus(agentBillVo.getStatus());
            }
        }



        //根据手机号、姓名的信息精确匹配，公司名称模糊匹配查询代理业绩总数
        int count = agentBillService.readPerformanceCount(agentBill,agentBillVo.getFromTime(),agentBillVo.getStopTime());

        if(count == 0){
            return fail("没有代理业绩结算记录");
        }

        //根据手机号、姓名的信息精确匹配，公司名称模糊匹配查询代理业绩
        List<AgentBill> list =  agentBillService.getAgentPerformance(agentBill,agentBillVo.getFromTime(),agentBillVo.getStopTime(),page2);

        if(list.size() <= 0){
            return fail("没有代理业绩结算记录");
        }

        //设置地区参数
        City city = null;

        List<AgentBill> resultList = new ArrayList<>();
        AgentInfo model = null;
        //当选择全部的时候
        if(null==agentBillVo.getStatus()){
        	agentBillVo.setStatus(100);
        }
        for (AgentBill bill : list){
            city = new City();
            city.setCode(bill.getAgentAreaId());
            City city1= sysCityService.readOne(city);
            if(null!=city1){
            	bill.setAgentAreaId(city1.getName());
            }
            //判断是否代理是否达到业绩结算日期,返回1表示达到，返回3表示未达到，不显示按钮(1:已结算，待打款 2:已打款 3:不打款 4:已删除)

            if(bill.getStatus().intValue() != 2){
                model = new AgentInfo();
                model.setAgentId(bill.getAgentId());
                AgentInfo agentInfo = agentInfoService.readOne(model);
                if(null!=agentInfo.getStatisticsTime()){
                	if(compare_date(agentInfo.getStatisticsTime(),new Date()) <= 0){
                		bill.setStatus(3);
                	}
                }
            }
            if(agentBillVo.getStatus().intValue() == 1){
                if(bill.getStatus().intValue()==3){
                    continue;
                }
            }else if(agentBillVo.getStatus().intValue() == 3){
                if(bill.getStatus().intValue()==1){
                    continue;
                }
            }
            resultList.add(bill);
        }
        //状态3分页
        if(agentBillVo.getStatus().intValue() == 3||agentBillVo.getStatus().intValue() == 1){
            //返回列表
            List<AgentBill> resultList2 = new ArrayList<>();
            for(int i=((page.getPageNo()-1)*page.getPageSize());i<(page.getPageSize());i++){
            	if(i>(resultList.size()-1)){
            		break;
            	}
                resultList2.add(resultList.get(i));
            }
            return success(getPageResult(page, resultList2.size(), resultList2));
        }else{
            //返回列表
            return success(getPageResult(page, count, resultList));
        }
    }
    
  //下载
 	@RequestMapping("/extAgentBill")
    public void extScoreRecords(AgentBillVo agentBillVo, Page page, HttpServletResponse response) {
        String fileName = "代理业绩记录统计" + RandomUtil.randomString(5) + ".csv";
        response.setContentType("application/CSV");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.addHeader("Cache-Control", "must-revalidate");
        response.addHeader("Pragma", "must-revalidate");
        OutputStream outputStream = null;
        
        AgentBill agentBill = new AgentBill();

        if(!ToolUtil.isEmpty(agentBillVo.getPhone())){
            agentBill.setPhone(agentBillVo.getPhone());
        }
        if(!ToolUtil.isEmpty(agentBillVo.getAgentLevel())){
            agentBill.setAgentLevel(agentBillVo.getAgentLevel());
        }
        if(!ToolUtil.isEmpty(agentBillVo.getAgentProvince())){
            agentBill.setAgentProvince(agentBillVo.getAgentProvince());
        }
        if(!ToolUtil.isEmpty(agentBillVo.getAgentCity())){
            agentBill.setAgentCity(agentBillVo.getAgentCity());
        }
        if(!ToolUtil.isEmpty(agentBillVo.getAgentCountry())){
            agentBill.setAgentCountry(agentBillVo.getAgentCountry());
        }
        if(!ToolUtil.isEmpty(agentBillVo.getStatus())){
            agentBill.setStatus(agentBillVo.getStatus());
        }
        try {
            outputStream = response.getOutputStream();
            outputStream.write("id,结算单号,收款公司名称,姓名,手机号,银行账户,银行,开户支行,代理区域,销售业绩,ep业绩,总业绩,状态,结算日期\r\n".getBytes());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            int count =  agentBillService.readPerformanceCount(agentBill,agentBillVo.getFromTime(),agentBillVo.getStopTime());
          /*  if (count == 0) {
                return;
            }*/
            //设置地区参数
            City city = null;
            List<AgentBill> list =   agentBillService.getAgentPerformance(agentBill,agentBillVo.getFromTime(),agentBillVo.getStopTime(),page);
            List<AgentBill> resultList = new ArrayList<>();
            for (AgentBill bill : list){
                city = new City();
                city.setCode(bill.getAgentAreaId());
                City city1= sysCityService.readOne(city);
                bill.setAgentAreaId(city1.getName());
                resultList.add(bill);
               
                
            }

            
            
           
            String type = "";
            for (AgentBill vo : resultList) {
            	
            	String status = "";
            	if(vo.getStatus() == 1){
            		status = "已补贴";
            	}else if(vo.getStatus() == 0){
            		status = "未补贴";
            	}else{
            		
            	}
            	
            	
            	
            	outputStream.write((vo.getId() + ",\t" + vo.getOrderNo() + "," +vo.getCompany()+","+vo.getUserName()
            	
            	+","+vo.getPhone()+","+vo.getBankCard()+","+vo.getBankType()+","+vo.getBankBranch()+","+vo.getAgentAreaId()+","+vo.getBalance()+","+vo.getEP()+","+vo.getTotalAmount()+","+status+","+DateTimeUtil.formatDate(vo.getUpdateTime(),DateTimeUtil.PATTERN_LONG)
                +"\r\n").getBytes());
            	outputStream.flush();
            	
            	
            	
                /*outputStream.write((vo.getId() + ",\t" + vo.getLoginName().toString() +"," + vo.getCompany() + "," +vo.getUserName()+","+vo.getPhone()
                        +"," +vo.getServicePhone()+","+vo.getEmail()+","+agentLevel+","+vo.getAgentAreaId()+","+vo.getAgentProvinceName()+","+vo.getAgentCityName()+","+vo.getAgentCountryName()+","+vo.getAddress()+","+DateTimeUtil.formatDate(vo.getCreateTime(),DateTimeUtil.PATTERN_LONG)+","+status+"\r\n").getBytes());
                outputStream.flush();*/
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    


    /**
     * 循环修改ep业绩记录
     * @param agentBillVo
     * @return
     */
    @RequestMapping("/updateByIds")
    @ResponseBody
    public JsonResult updateByIds(AgentBillVo agentBillVo) throws Exception{

        if(ToolUtil.isEmpty(agentBillVo.getIds())){
            return fail("至少选择一个代理");
        }

        AgentBill agentBill = new AgentBill();
        /*1:已结算，待打款 2:已打款 3:不打款 4:已删除*/
        agentBill.setStatus(2);
        agentBillService.updateByIds(agentBill,agentBillVo.getIds());

        //修改代理的结算时间
        for (String id : agentBillVo.getIds()){
            AgentBill bill = agentBillService.readById(id);
            AgentInfo agentInfo = agentInfoService.readById(bill.getAgentId());
            SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            AgentInfo model = new AgentInfo();
            model.setStatisticsTime(simple.parse(simple.format(DateTimeUtil.getDayThirty(agentInfo.getStatisticsTime()))));
            agentInfoService.updateById(agentInfo.getAgentId(),model);
        }

        //返回列表
        return success();
    }

    public static int compare_date(Date DATE1, Date DATE2) {

        try {
        	//返回小于等于0为不打款
            if (DATE1.getTime() > DATE2.getTime()) {
                System.out.println("DATE1 在DATE2前");
                return -1;
            } else if (DATE1.getTime() < DATE2.getTime()) {
                System.out.println("DATE1在DATE2后");
                return 1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.print("http://192.168.2.202:8889/agent/agentInfo/testInAgentStatistics?startTime=''&endTime=''");
    }

}