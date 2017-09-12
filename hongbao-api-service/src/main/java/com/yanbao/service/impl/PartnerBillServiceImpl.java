package com.yanbao.service.impl;

import com.mall.model.*;
import com.yanbao.constant.MessageType;
import com.yanbao.constant.RecordType;
import com.yanbao.constant.StatusType;
import com.yanbao.dao.PartnerBillDao;
import com.yanbao.service.MessageService;
import com.yanbao.service.PartnerBillService;
import com.yanbao.service.UserService;
import com.yanbao.service.WalletRecordService;
import com.yanbao.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author jay.zheng
 * @date 2017年08月01日
 */
@Service
public class PartnerBillServiceImpl implements PartnerBillService {

    private static final Logger logger = LoggerFactory.getLogger(PartnerBillServiceImpl.class);

    private static final String SYSTEM_USER_USERID = "system";

    @Autowired
    private PartnerBillDao partnerBillDao;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private WalletRecordService walletRecordService;

    @Override
    public Integer create(PartnerBill model) throws Exception {
        if (model == null || model.getPartnerId() == null) {
            return null;
        }
        setDefault(model);
        return partnerBillDao.create(model);
    }

    private void setDefault(PartnerBill model) throws Exception {
        model.setId(UUIDUtil.getUUID());
        if (model.getOrderNo() == null) {
            model.setOrderNo(OrderNoUtil.get());
        }
        model.setCreateTime(new Date());
        model.setStatus(StatusType.TRUE.getCode());
    }

    @Override
    public List<PartnerBill> getAllPartner(String startTime, String endTime) {
        if (ToolUtil.isEmpty(startTime) || ToolUtil.isEmpty(endTime)) {
            return null;
        }
        return partnerBillDao.getAllPartners(startTime, endTime);
    }

    @Override
    public List<PartnerBill> getPartners(String startTime, String endTime) {
        if (ToolUtil.isEmpty(startTime) || ToolUtil.isEmpty(endTime)) {
            return null;
        }
        return partnerBillDao.getPartners(startTime, endTime);
    }

    /**
     * 结算业绩（提现+商家赠送EP）
     *
     * @param startTime 开始时间（YYYY-MM-DD）
     * @param endTime   结束时间（YYYY-MM-DD）
     */
    @Transactional
    @Override
    public void inPartnerStatistics(String startTime, String endTime) throws Exception {
        //1.查找上月所有合伙人获得的业绩提成
        List<PartnerBill> list = this.getPartners(startTime, endTime);
        double epScale = PoundageUtil.divide(ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.EPSCALE), 0d), 100, 4);
        double balanceScale = PoundageUtil.divide(ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.BALANCESCALE), 0d), 100, 4);
        if (epScale <= 0 && balanceScale <= 0) {
            logger.info("后台系统参数【epScale，balanceScale】未设置。");
            return;
        }
        for (PartnerBill bill : list) {
            if (bill.getTotalAmount() != null && bill.getTotalAmount() > 0) {
                String orderNo = OrderNoUtil.get();
                User user = userService.getById(bill.getPartnerId());
                if (user == null) {
                    logger.info("找不到用户或用户已删除");
                    continue;
                }
                bill.setEPScale(epScale);
                bill.setBalanceScale(balanceScale);
                bill.setOrderNo(orderNo);
                bill.setGrade(user.getGrade());
                bill.setAreaId(user.getAreaId());
                bill.setCity(user.getCity());
                bill.setCountry(user.getCounty());
                bill.setProvince(user.getProvince());
                bill.setPhone(user.getPhone());
                bill.setUserName(user.getUserName());
                bill.setBillday(startTime.replace("-", "").substring(0, 6));//记录结算月份
                bill.setUid(user.getUid());
                //结算总金额
                Double totalAmount = PoundageUtil.getPoundage(bill.getBalance() * bill.getBalanceScale() + bill.getEP() * bill.getEPScale(), 1d);
                bill.setTotalAmount(totalAmount);
                //2.合伙人结算业绩获得的金额
                userService.updateScore(bill.getPartnerId(), bill.getTotalAmount());
                //3.从系统账户扣减金额
                userService.updateScore(SYSTEM_USER_USERID, -bill.getTotalAmount());
                //4.发送消息
                Message scoreMessage = new Message();
                scoreMessage.setUserId(bill.getPartnerId());
                scoreMessage.setOrderNo(orderNo);
                scoreMessage.setTitle(MessageType.SYSTEM.getMsg());
                scoreMessage.setType(MessageType.SYSTEM.getCode());
                scoreMessage.setDetail("合伙人每月业绩结算，您上月总共获得" + bill.getTotalAmount() + "金额!");
                scoreMessage.setRemark("合伙人业绩结算");
                messageService.add(scoreMessage);
                //5.合伙人金额来源的流水记录
                WalletRecord record = new WalletRecord();
                record.setUserId(bill.getPartnerId());
                record.setOrderNo(orderNo);
                record.setScore(bill.getTotalAmount());
                record.setRecordType(RecordType.PARTNER_GAIN_SCORE.getCode());
                record.setRemark("合伙人每月业绩结算，您上月总共获得" + bill.getTotalAmount() + "金额!");
                walletRecordService.add(record);
                //6.系统扣除金额的流水记录
                WalletRecord sysRescord = new WalletRecord();
                sysRescord.setOrderNo(orderNo);
                sysRescord.setScore(-bill.getTotalAmount());
                sysRescord.setUserId(SYSTEM_USER_USERID);
                sysRescord.setRecordType(RecordType.PARTNER_GAIN_SCORE.getCode());
                sysRescord.setRemark("合伙人【" + user.getUid() + "】每月业绩结算，上月总共获得" + bill.getTotalAmount() + "金额!系统扣除:" + bill.getTotalAmount() + "金额");
                walletRecordService.add(sysRescord);
                //7.增加合伙人每月结算记录
                this.create(bill);
            }
        }
    }
}
