package com.yanbao.service.impl;


import com.mall.model.*;
import com.yanbao.constant.*;
import com.yanbao.core.page.Page;
import com.yanbao.dao.WalletSignDao;
import com.yanbao.service.*;
import com.yanbao.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author zcj
 * @date 2017年03月03日
 */
@Service
public class WalletSignServiceImpl implements WalletSignService {
    private static final String SYSUSERID = "system";
    @Autowired
    private WalletSignDao walletSignDao;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletRecordService walletRecordService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EPRecordService epRecordService;

    @Override
    public List<WalletSign> getList(String userId, Page page) throws Exception {
        List<WalletSign> list = walletSignDao.getList(userId, page);
        return CollectionUtils.isEmpty(list) ? Collections.EMPTY_LIST : list;
    }

    @Override
    public Integer count(String userId) throws Exception {
        return walletSignDao.count(userId);
    }

    @Override
    public Integer add(WalletSign model) throws Exception {
        if (model == null) {
            return null;
        }
        setDefaultValue(model);
        return walletSignDao.add(model);
    }

    private void setDefaultValue(WalletSign model) {
        model.setId(UUIDUtil.getUUID());
        model.setOrderNo(OrderNoUtil.get());
        model.setCreateTime(new Date());
    }

    @Override
    public Double signTotal(WalletSign model, Integer countNo) throws Exception {
        Double total = walletSignDao.signTotal(model, countNo);
        if (total == null) {
            total = 0.0d;
        }
        return total;
    }

    @Override
    public Integer getSubsidyCount(String userId, String grade) throws Exception {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(grade)) {
            return 0;
        }
        Integer count = walletSignDao.getSubsidyCount(userId, grade);
        if (count == null) {
            count = 0;
        }
        return count;
    }

    @Override
    public Integer getSignCount(String userId) throws Exception {
        if (StringUtils.isEmpty(userId)) {
            return 0;
        }
        Integer signCount = walletSignDao.getSignCount(userId);
        if (signCount == null) {
            signCount = 0;
        }
        return signCount;
    }

    @Override
    public List<WalletSign> getCommonSignList(String userId, Page page) {
        List<WalletSign> list = walletSignDao.getCommonSignList(userId, page);
        return CollectionUtils.isEmpty(list) ? Collections.EMPTY_LIST : list;
    }

    @Override
    public List<WalletSign> getMyDoudouList(String userId, Page page) {
        List<WalletSign> list = walletSignDao.getMyDoudouList(userId, page);
        return CollectionUtils.isEmpty(list) ? Collections.EMPTY_LIST : list;
    }

    @Override
    public Integer countMyDoudouListSize(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return 0;
        }
        Integer count = walletSignDao.countMyDoudouListSize(userId);
        if (count == null) {
            count = 0;
        }
        return count;
    }

    @Override
    public Integer countCommonSignListSize(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return 0;
        }
        Integer count = walletSignDao.countCommonSignListSize(userId);
        if (count == null) {
            count = 0;
        }
        return count;
    }

    @Override
    @Transactional
    public Double doudouSignIn(String userId) throws Exception {
        String orderNo=OrderNoUtil.get();
        User user = userService.getById(userId);
        ParamUtil util = ParamUtil.getIstance();
        double minSignDouNum = ToolUtil.parseDouble(util.get(Parameter.MINSIGNDOUNUM), 0d);
        double commonReleaseScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.COMMONRELEASESCALE), 0d),100,4);
        double vipReleaseScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.VIPRELEASESCALE), 0d),100,4);
        //用户持有斗斗数量小于最少签到斗斗数量，不能签到
        if (user.getDoudou() < minSignDouNum ) {
            return 0d;
        }
        //普通会员每天签到可领取的金额=持有的斗斗数量*普通会员兑换金额的比例
        Double doudou = user.getDoudou() * commonReleaseScale;
        //加入过合伙人的
        if (user.getGrade() >= 1) {
            doudou = user.getDoudou() * vipReleaseScale;
        }
        doudou = PoundageUtil.getPoundage(doudou, 1d);
        //领取斗斗释放的金额 以前是加到用户余额  现在改为加到用户的EP账户 updateScore
        userService.updateEpById(user.getId(), doudou);
        //加到EP后，同时增加用户的EP流水记录
        epRecordService.consumeEpRecord(user,PoundageUtil.getPoundage(doudou,1d),orderNo, EPRecordType.DOUDOU_SIGNIN,Constant.SYSTEM_USERID,null,"斗斗每日签到,用户EP增加");
        //更新斗斗数量和签到时间
        userService.updateDoudou(user.getId(), - doudou, new Date());
        //记录斗斗每次签到的信息
        WalletSign sign = new WalletSign();
        sign.setScore(doudou);
        sign.setConfirmScore(doudou);
        sign.setType(SignType.DOUDOU_SIGN.getCode());
        sign.setRemark("斗斗每日签到");
        sign.setGrade(user.getGrade());
        sign.setUserId(user.getId());
        sign.setDonateUid(user.getUid());
        sign.setDonateUserId(user.getId());
        sign.setDoudou(doudou);
        sign.setStatus(StatusType.TRUE.getCode());
        sign.setScale(user.getGrade() >= 1 ? vipReleaseScale : commonReleaseScale);
        this.add(sign);
        //保存流水记录
        WalletRecord record = new WalletRecord();
        record.setUserId(user.getId());
        record.setRecordType(RecordType.SIGN_DOUDOU.getCode());
        record.setScore(doudou);
        record.setRemark(RecordType.SIGN_DOUDOU.getMsg());
        record.setOrderNo(OrderNoUtil.get());
        walletRecordService.add(record);
        //发送消息提醒
        Message message = new Message();
        message.setRemark(RecordType.SIGN_DOUDOU.getMsg());
        message.setUserId(user.getId());
        message.setTitle(RecordType.SIGN_DOUDOU.getMsg());
        message.setType(MessageType.SYSTEM.getCode());
        message.setDetail("斗斗签到成功，领取：" + doudou + "EP");
        message.setOrderNo(OrderNoUtil.get());
        messageService.add(message);
        //领多少金额就扣除系统账户对应的EP
        User sysUser = userService.getById(SYSUSERID);
        userService.updateEp(sysUser.getId(),-PoundageUtil.getPoundage(doudou,1d));
        //记录系统账户被扣除EP的记录
        epRecordService.consumeEpRecord(sysUser,-PoundageUtil.getPoundage(doudou,1d),sign.getOrderNo(), EPRecordType.DOUDOU_SIGNIN,Constant.SYSTEM_USERID,null,"斗斗每日签到,扣除对应斗斗数量的EP");
        return doudou;
    }
}
