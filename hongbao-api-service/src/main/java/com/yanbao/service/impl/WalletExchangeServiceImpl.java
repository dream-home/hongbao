package com.yanbao.service.impl;

import com.mall.model.*;
import com.yanbao.constant.*;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.dao.WalletExchangeDao;
import com.yanbao.service.*;
import com.yanbao.util.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class WalletExchangeServiceImpl implements WalletExchangeService {

    @Autowired
    private WalletExchangeDao walletExchangeDao;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletRecordService walletRecordService;
    @Autowired
    private ConsumeEPRecordService consumeEPRecordService;
    @Autowired
    private WalletSignService walletSignService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EPRecordService ePRecordService;
    @Autowired
    private PerformanceRecordService performanceRecordService;

    @Override
    public WalletExchange getByOrderNo(String orderNo, String userId) throws Exception {
        if (StringUtils.isBlank(orderNo)) {
            return null;
        }
        return walletExchangeDao.getByOrderNo(orderNo, userId);
    }

    @Override
    public PageResult<WalletExchange> getPage(String userId, Page page) throws Exception {
        if (userId == null) {
            return null;
        }
        PageResult<WalletExchange> pageResult = new PageResult<WalletExchange>();
        BeanUtils.copyProperties(pageResult, page);

        Integer count = walletExchangeDao.count(userId);
        pageResult.setTotalSize(count);
        if (count != null && count > 0) {
            List<WalletExchange> list = walletExchangeDao.getList(userId, page);
            pageResult.setRows(list);
        }
        return pageResult;
    }

    @Override
    public Integer add(WalletExchange model) throws Exception {
        if (model == null) {
            return null;
        }
        setDefaultValue(model);
        return walletExchangeDao.add(model);
    }

    @Override
    public Integer update(String id, WalletExchange model) throws Exception {
        if (StringUtils.isBlank(id) || model == null) {
            return 0;
        }
        model.setUpdateTime(new Date());
        return walletExchangeDao.update(id, model);
    }

    private void setDefaultValue(WalletExchange model) {
        model.setId(UUIDUtil.getUUID());
        model.setOrderNo(OrderNoUtil.get());
        model.setStatus(ExchangeType.PENDING.getCode());
        model.setCreateTime(new Date());
    }

    @Override
    @Transactional
    public Boolean exchangeHandler(User user, Double score, UserBankcard bankcard) throws Exception {
        // 计算手续费
        Double poundageScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.EXCHANGEPOUNDAGESCALE), 0.05d);
        if (poundageScale == null) {
            poundageScale = 0.05;
        }
        Double poundage = PoundageUtil.getPoundage(score, poundageScale);
        // 修改用户积分
        userService.updateScore(user.getId(), -score);
        // 增加兑换记录
        WalletExchange exchange = new WalletExchange();
        exchange.setUserId(user.getId());
        exchange.setScore(-score);
        exchange.setPoundage(poundage);
        exchange.setConfirmScore(score - poundage);
        exchange.setBankName(bankcard.getBankName());
        exchange.setBankId(bankcard.getBankId());
        exchange.setCardType(bankcard.getType());
        exchange.setCardNo(bankcard.getCardNo());
        this.add(exchange);
        // 增加积分流水
        WalletRecord record = new WalletRecord();
        record.setUserId(user.getId());
        record.setOrderNo(exchange.getOrderNo());
        record.setScore(-score);
        record.setRecordType(RecordType.EXCHANGE.getCode());
        record.setRemark(RecordType.EXCHANGE.getMsg());
        walletRecordService.add(record);
        return true;
    }


    @Override
    @Transactional
    public Boolean exchangeHandlerForWeiXin(User user, Double score) throws Exception {
        // 计算手续费
        Double poundageScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.EXCHANGEPOUNDAGESCALE), 0.05d);
        if (poundageScale == null) {
            poundageScale = 0.05;
        }
        Double poundage = PoundageUtil.getPoundage(score, poundageScale);
        // 修改用户积分
        userService.updateScore(user.getId(), -score);
        // 增加兑换记录
        WalletExchange exchange = new WalletExchange();
        exchange.setUserId(user.getId());
        exchange.setScore(-score);
        exchange.setPoundage(poundage);
        exchange.setConfirmScore(score - poundage);
        exchange.setBankName("加入合伙人现金部分给代理提成");
        exchange.setBankId("");
        exchange.setCardType(BankCardType.JOIN_DONATE_FOR_AGENT.getCode());
        exchange.setCardNo("");
        exchange.setRemark("加入合伙人现金部分给代理提成");
        this.add(exchange);
        // 增加积分流水
        WalletRecord record = new WalletRecord();
        record.setUserId(user.getId());
        record.setOrderNo(exchange.getOrderNo());
        record.setScore(-score);
        record.setRecordType(RecordType.EXCHANGE.getCode());
        record.setRemark(RecordType.EXCHANGE.getMsg());
        walletRecordService.add(record);
        return true;
    }

    /***
     *
     * EP 兑换成用户斗斗
     * @param user
     * @param ep
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Boolean epToDouDouExchangeHandler(User user, Double ep) throws Exception {

        if (user.getExchangeEP() < ep) {
            throw new RuntimeException("EP不足 ,无法完成兑换");
        }
        Double doudou = PoundageUtil.getPoundage(ep * ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.EPTODOUSCALE), 0d), 1d);
        WalletSign walletSign = epToDoudou(user, doudou);
        // 扣减用户EP
        userService.updateEpById(user.getId(), -ep);
        //增加用户消费ep流水表
        epToDoudouConsumeEpRecord(user, ep, walletSign);
        //增加自己的消费EP
        userService.updateConsumeEPById(user.getId(), ep);
        // 修改用户斗斗
        userService.updateDoudou(user.getId(), doudou);
        // 增加系统EP收入维系EP平衡
        userService.updateEpById(Constant.SYSTEM_USERID, ep);
        //增加兑换斗斗消息
        epToDoudouMessage(user, ep, walletSign, doudou);
        // 增加EP兑换斗斗流水
        epToDoudouEpRecord(user, ep, walletSign);
        //兑换斗斗成功，向上累加EP销售业绩
        userService.updatePerformanceCount(user.getId(), ep);
        //新增业绩处理记录
        performanceRecordService.create(walletSign.getOrderNo(),ep,user.getId(),null,"用户使用EP兑换斗斗");
        return true;
    }

    private void epToDoudouConsumeEpRecord(User user, Double ep, WalletSign walletSign) throws Exception {
        ConsumeEPRecord epRecord = new ConsumeEPRecord();
        epRecord.setConsumeEp(ep);
        epRecord.setOrderNo(walletSign.getOrderNo());
        epRecord.setRemark(ConsumeEPType.EP_TO_DOU.getMsg());
        epRecord.setUserId(user.getId());
        epRecord.setGrade(user.getGrade());
        epRecord.setType(ConsumeEPType.EP_TO_DOU.getCode());
        consumeEPRecordService.add(epRecord);
    }

    private void epToDoudouMessage(User user, Double ep, WalletSign walletSign, Double doudou) throws Exception {
        Message message = new Message();
        message.setUserId(user.getId());
        message.setOrderNo(walletSign.getOrderNo());
        message.setTitle(RecordType.EP_TO_DOUDOU.getMsg());
        message.setType(MessageType.SYSTEM.getCode());
        message.setDetail("兑换斗斗消耗了" + ep + "EP,获得了" + doudou + "斗斗");
        message.setRemark(RecordType.EP_TO_DOUDOU.getMsg());
        messageService.add(message);
    }

    private void epToDoudouEpRecord(User user, Double ep, WalletSign walletSign) throws Exception {
        EpRecord record = new EpRecord();
        record.setSendUserId(user.getId());
        record.setReceiveUserId(Constant.SYSTEM_USERID);
        record.setOrderNo(walletSign.getOrderNo());
        record.setEp(-ep);
        record.setRecordType(EPRecordType.EP_TO_DOUDOU.getCode());
        record.setRemark(EPRecordType.EP_TO_DOUDOU.getMsg());
        record.setRecordTypeDesc(EPRecordType.EP_TO_DOUDOU.getMsg());
        if (ToolUtil.isEmpty(user.getStoreId())) {
            record.setUserType(UserType.COMMON.getCode());
        }else if(!ToolUtil.isEmpty(user.getStoreId())){
            record.setUserType(UserType.STORE.getCode());
        }
        ePRecordService.add(record);
    }

    //生成兑换订单
    private WalletSign epToDoudou(User user, Double ep) throws Exception {
        WalletSign walletSign = new WalletSign();
        walletSign.setUserId(user.getId());
        walletSign.setDonateUserId(user.getId());
        walletSign.setDonateUid(user.getUid());
        walletSign.setScore(ep);
        walletSign.setConfirmScore(ep);
        walletSign.setStatus(StatusType.TRUE.getCode());
        walletSign.setSignNo(0);
        Boolean flag = (user.getRemainSign() > 0 && user.getGrade() > 0) || (user.getRemainSign() == 0 && user.getSignTime() == null);
        if (flag) {
            walletSign.setGrade(user.getGrade());
        } else {
            walletSign.setGrade(GradeType.grade2.getCode());
        }
        walletSign.setSignCount(0);
        walletSign.setType(SignType.EP_TO_DOU.getCode());
        walletSign.setRemark(SignType.EP_TO_DOU.getMsg());
        //EP转斗斗比例
        walletSign.setScale(ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.EPTODOUSCALE), 0d));
        walletSign.setDoudou(ep);
        walletSignService.add(walletSign);
        return walletSign;
    }

    @Override
    public Integer countCurrentDay(String userId) throws Exception {
        return walletExchangeDao.countCurrentDay(userId);
    }
}
