package com.yanbao.service.impl;

import com.yanbao.constant.*;
import com.yanbao.core.model.JpushExtraModel;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.dao.WalletRechargeDao;
import com.mall.model.*;
import com.yanbao.redis.Hash;
import com.yanbao.redis.Sets;
import com.yanbao.service.*;
import com.yanbao.util.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanbao.constant.BankCardType.JOIN_DONATE_FOR_AGENT;

/**
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class WalletRechargeServiceImpl implements WalletRechargeService {

    private static final Logger logger = LoggerFactory.getLogger(WalletRechargeServiceImpl.class);
    private static final String SYSTEM = "system";

    @Autowired
    private WalletRechargeDao walletRechargeDao;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletRecordService walletRecordService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SysSettingService sysSettingService;
    @Autowired
    private PayDistributionService payDistributionService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private ConsumeEPRecordService consumeEPecordService;
    @Autowired
    private EPRecordService epRecordService;
    @Autowired
    private WalletSignService walletSignService;
    @Autowired
    private PerformanceRecordService performanceRecordService;
    @Autowired
    private PartnerBillDetailService partnerBillDetailService;
    @Autowired
    private WalletExchangeService walletExchangeService;

    @Override
    public WalletRecharge getByOrderNo(String orderNo, String userId) throws Exception {
        if (StringUtils.isBlank(orderNo)) {
            return null;
        }
        return walletRechargeDao.getByOrderNo(orderNo, userId);
    }

    @Override
    public WalletRecharge getUserOrderByOrderNo(String orderNo) throws Exception {
        if (StringUtils.isBlank(orderNo)) {
            return null;
        }
        return walletRechargeDao.getUserOrderByOrderNo(orderNo);
    }


    @Override
    public PageResult<WalletRecharge> getPage(String userId, Page page) throws Exception {
        if (userId == null) {
            return null;
        }
        PageResult<WalletRecharge> pageResult = new PageResult<WalletRecharge>();
        BeanUtils.copyProperties(pageResult, page);

        Integer count = walletRechargeDao.count(userId);
        pageResult.setTotalSize(count);
        if (count != null && count > 0) {
            List<WalletRecharge> list = walletRechargeDao.getList(userId, page);
            pageResult.setRows(list);
        }
        return pageResult;
    }

    @Override
    public Integer add(WalletRecharge model) throws Exception {
        if (model == null) {
            return null;
        }
        setDefaultValue(model);
        return walletRechargeDao.add(model);
    }

    @Override
    public Integer update(String id, WalletRecharge model) throws Exception {
        if (StringUtils.isBlank(id) || model == null) {
            return 0;
        }
        model.setUpdateTime(new Date());
        return walletRechargeDao.update(id, model);
    }

    private void setDefaultValue(WalletRecharge model) {
        model.setId(UUIDUtil.getUUID());
        model.setOrderNo(OrderNoUtil.get());
        if (model.getStatus() == null || model.getStatus() == 0) {
            model.setStatus(RechargeType.PENDING.getCode());
        }
        model.setCreateTime(new Date());
    }

    @Override
    @Transactional
    public Boolean rechargeHandler(User user, String orderNo) throws Exception {
        WalletRecharge model = this.getByOrderNo(orderNo, user.getId());

        if (null != model && model.getSource().intValue() == BankCardType.SCAN_CODE_BALANCE.getCode()) {
            return true;
        } else {
            if (null == model || model.getStatus() != RechargeType.PENDING.getCode() || model.getSource() == null) {
                logger.error(String.format("Illegal recharge orderNo[%s]", orderNo));
                throw new IllegalArgumentException();
            }
        }
        // 修改充值订单
        model.setStatus(RechargeType.SUCCESS.getCode());
        this.update(model.getId(), model);
        // 修改用户积分
        userService.updateScore(user.getId(), model.getConfirmScore());
        // 增加积分流水
        WalletRecord record = new WalletRecord();
        record.setUserId(model.getUserId());
        record.setOrderNo(model.getOrderNo());
        record.setScore(model.getScore());
        record.setRecordType(RecordType.RECHARGE.getCode());
        record.setRemark(RechargeType.SUCCESS.getMsg());
        walletRecordService.add(record);
        // 添加充值消息
        Message message = new Message();
        message.setUserId(user.getId());
        message.setOrderNo(orderNo);
        message.setTitle("充值成功");
        message.setType(MessageType.RECHARGE.getCode());
        message.setDetail("本次充值" + model.getConfirmScore() + "金额");
        message.setRemark(MessageType.RECHARGE.getMsg());
        messageService.add(message);
        // websocket
        Hash.hincrby(RedisKey.ALL_IDS.getKey(), user.getId(), 1);
        Sets.sadd(RedisKey.WAITING_IDS.getKey(), user.getId());
        return true;
    }

    @Override
    @Transactional
    public Boolean scanCodeHandler(User user, String orderNo) throws Exception {
        Boolean flag = this.rechargeHandler(user, orderNo);
        user = userService.getById(user.getId());
        if (flag) {
            WalletRecharge model = this.getByOrderNo(orderNo, user.getId());
            if (null == model || model.getStatus() != RechargeType.SUCCESS.getCode()) {
                logger.error(String.format("Illegal recharge orderNo[%s]", orderNo));
                throw new IllegalArgumentException();
            }
            if (user.getScore() == null || user.getScore() < model.getConfirmScore()) {
                throw new RuntimeException("用户余额不足");
            }

            //判断用户是否ep折扣优惠全额支付
            if (model.getConfirmScore() != 0) {
                //扣减用户积分
                userService.updateScore(user.getId(), -model.getConfirmScore());
                //扣减用户积分流水
                addUserScoreRecord(user.getId(), model.getOrderNo(), -model.getConfirmScore(), RecordType.SCANCODE_USER.getCode(), RecordType.SCANCODE_USER.getMsg());
                //扣减用户积分消息
                String userDetail = "扫码支付成功，支出金额" + model.getConfirmScore();
                addUserScoreAndEpMessage(user.getId(), model.getOrderNo(), MessageType.SCANCODE.getMsg(), MessageType.SCANCODE.getCode(), userDetail, MessageType.SCANCODE.getMsg());

                //三级分销业务
                double sumScore = this.dealDistribution(user, model);

                BigDecimal bd1 = new BigDecimal(Double.toString(model.getConfirmScore()));
                BigDecimal bd2 = new BigDecimal(Double.toString(sumScore));
                model.setConfirmScore(bd1.subtract(bd2).doubleValue());
                //系统抽取费用
                Double tradeScore = userService.tradeRate(model.getOrderNo(), model.getStoreUserId(), model.getConfirmScore());
                //商家所得积分=总交易积分-三级分销-赠送EP-系统抽取费用
                Double score = model.getConfirmScore() - tradeScore;
                score = PoundageUtil.getPoundage(score, 1d);
                model.setConfirmScore(score);
                //增加商户积分
                userService.updateScore(model.getStoreUserId(), model.getConfirmScore());
                //增加商户积分流水
                addUserScoreRecord(model.getStoreUserId(), model.getOrderNo(), model.getConfirmScore(), RecordType.SCANCODE_STORE.getCode(), RecordType.SCANCODE_STORE.getMsg());
                //增加商户积分消息
                String storeDetail = "用户扫码支付成功，收到" + model.getConfirmScore() + "金额";
                addUserScoreAndEpMessage(model.getStoreUserId(), model.getOrderNo(), MessageType.SCANCODE.getMsg(), MessageType.SCANCODE.getCode(), storeDetail, MessageType.SCANCODE.getMsg());
            }

            //增加用户消费ep流水
            this.addSystemEp(user, orderNo, PayDistributionType.member.getCode());

            if (model.getBusinessSendEp() != null && model.getBusinessSendEp() > 0) {
                Double ep = PoundageUtil.getPoundage(model.getScore() * model.getBusinessSendEp() / 100d, 1d);
                if (ep > 0) {
                    userService.updateEpById(user.getId(), ep);
                    //增加用户ep流水
                    addUserScoreRecord(user.getId(), model.getOrderNo(), ep, RecordType.SCANCODE_EP.getCode(), RecordType.SCANCODE_EP.getMsg());
                    //增加EP流水记录
                    //面对面扫码支付收款账户记录,赠送EP
                    epRecordService.consumeEpRecord(userService.getById(model.getStoreUserId()),-ep,model.getOrderNo(),EPRecordType.SCANCODE_EP,model.getStoreUserId(),user.getId(),"");
                    epRecordService.consumeEpRecord(user,ep,model.getOrderNo(),EPRecordType.SCANCODE_EP,user.getId(),model.getStoreUserId(),"");
                    //增加用户ep消息
                    String detail = "扫码支付成功，获赠" + ep + "EP";
                    addUserScoreAndEpMessage(user.getId(), model.getOrderNo(), MessageType.SCANCODE_EP.getMsg(), MessageType.SYSTEM.getCode(), detail, MessageType.SCANCODE_EP.getMsg());

                    //新增赠送与ep等额的斗斗给会员的记录
                    /*WalletRecord doudouRecord = new WalletRecord();
                    doudouRecord.setUserId(user.getId());
                    doudouRecord.setOrderNo(model.getOrderNo());
                    doudouRecord.setScore(model.getDoudou());
                    doudouRecord.setRecordType(RecordType.SCANCODE_DOUDOU.getCode());
                    doudouRecord.setRemark(RecordType.SCANCODE_DOUDOU.getMsg());
                    walletRecordService.add(doudouRecord);*/

                    //新增用户收到斗斗的记录
                  /*  WalletSign walletSign = new WalletSign();
                    walletSign.setUserId(user.getId());
                    walletSign.setDonateUserId(user.getId());
                    walletSign.setDonateUid(user.getUid());
                    walletSign.setDoudou(model.getDoudou());
                    walletSign.setOrderNo(model.getOrderNo());
                    walletSign.setScale(0d);
                    walletSign.setType(SignType.SCANCODE_DOUDOU.getCode());
                    walletSign.setRemark(SignType.SCANCODE_DOUDOU.getMsg());
                    walletSign.setScore(model.getDoudou());
                    walletSign.setConfirmScore(model.getDoudou());
                    walletSign.setGrade(user.getGrade());
                    walletSign.setStatus(StatusType.TRUE.getCode());
                    walletSignService.add(walletSign);*/

                    //增加用户的斗斗值
//                    userService.updateDoudou(user.getId(),model.getDoudou());

                    //无ep折扣优惠,创建ep业绩赠送明细
                    User storeUser = userService.getById(model.getStoreUserId());
                    //面对面扫码支付需要判断是否是商家
                    if(!ToolUtil.isEmpty(storeUser.getStoreId())){
                        partnerBillDetailService.createPartnerBillDetail(storeUser.getStoreId(),model.getOrderNo(),model.getBusinessSendEp());
                    }
                }
            }
            // 修改支付订单
            WalletRecharge updateModel = new WalletRecharge();
            updateModel.setStatus(RechargeType.TRANSFER_SUCCESS.getCode());
            this.update(model.getId(), updateModel);
            //用户扫码支付成功推送
            JPushUtil.pushPayloadByid(user.getRegistrationId(), "您有一笔金额为：" + model.getScore() + "的交易", new JpushExtraModel(JpushExtraModel.NOTIFIYPE, JpushExtraModel.EXPENSE_MSM));
            //商家扫码支付成功推送
            User store = userService.getById(model.getStoreUserId());
            JPushUtil.pushPayloadByid(store.getRegistrationId(), "您有一笔金额为：" + model.getScore() + "的交易", new JpushExtraModel(JpushExtraModel.NOTIFIYPE, JpushExtraModel.EXPENSE_MSM));
        }
        //异步处理60层销售业绩业务
        // 异步处理分组业务
       /* final String userId = user.getId();
        final User user1 = user;
        final String order1 = orderNo;
        new Thread(new Runnable() {
            public void run() {
                try {
                    //客户端微信注册登录，用户根据系统算法分组
                    userService.updatePermanceByThread(user1,order1,0d);
                } catch (Exception e) {
                    logger.error("处理分组失败！！" + e.getMessage());
                }
            }
        }).start();*/
        return true;
    }

    /**
     * 扣除用户EP，增加系统ep,EP抵扣
     */
    private void addSystemEp(User user, String orderNo, Integer payType) throws Exception {
        //获取最新的订单数据
        WalletRecharge walletRecharge = this.getByOrderNo(orderNo, user.getId());

        Double userEp = walletRecharge.getDiscountEP();

        // 计算Ep,修改用户ep
        if (userEp > 0d) {
            // 新增用户扣除ep流水记录
            user = userService.getById(user.getId());

            //扣除用户ep
            userService.updateEp(user.getId(), -userEp);

            //定义扣除用户ep折扣的流水
            WalletRecord EpRecord = new WalletRecord();
            //增加用户的ep消费ep
            userService.updateConsumeEPById(user.getId(), userEp);
            ConsumeEPRecord model = new ConsumeEPRecord();
            model.setId(UUIDUtil.getUUID());
            model.setCreateTime(new Date());
            model.setUpdateTime(new Date());
            model.setConsumeEp(userEp);
            model.setStatus(1);
            model.setUserId(user.getId());
            model.setGrade(user.getGrade());
            if (payType.intValue() == PayDistributionType.member.getCode().intValue()) {
                model.setRemark("面对面支付中使用了ep折扣优惠券，消费了" + userEp + "EP");
                EpRecord.setRemark("您在面对面支付中使用了ep折扣优惠券，消费了" + userEp + "EP");
                //增加EP流水表记录
                epRecordService.consumeEpRecord(user,-userEp,orderNo,EPRecordType.FACE_SCAN_DISCOUNT,user.getId(),Constant.SYSTEM_USERID,"");
            } else if (payType.intValue() == PayDistributionType.store.getCode().intValue()) {
                model.setRemark("商家二维码扫码支付中使用了ep折扣优惠券，消费了" + userEp + "EP");
                EpRecord.setRemark("您在商家二维码扫码支付中使用了ep折扣优惠券，消费了" + userEp + "EP");
                //增加EP流水表记录
                epRecordService.consumeEpRecord(user,-userEp,orderNo,EPRecordType.STORE_SCAN_DISCOUNT,user.getId(),Constant.SYSTEM_USERID,"");
            }
            consumeEPecordService.add(model);
            // 增加用户扣除EP记录
            EpRecord.setOrderNo(walletRecharge.getOrderNo());
            EpRecord.setScore(userEp);
            EpRecord.setUserId(user.getId());
            EpRecord.setRecordType(RecordType.USER_DISCOUNT_EP.getCode());
            walletRecordService.add(EpRecord);

            //上线会员增加销售业绩  改为线程处理
            userService.updatePerformanceCount(user.getId(), userEp);
            //新增业绩处理记录
            performanceRecordService.create(walletRecharge.getOrderNo(),userEp,user.getId(),walletRecharge.getSource(),walletRecharge.getRemark());

            // 新增用户使用折扣ep优惠消息
            Message message = new Message();
            message.setUserId(walletRecharge.getUserId());
            message.setOrderNo(walletRecharge.getOrderNo());
            message.setTitle(MessageType.EXPENSE.getMsg());
            message.setType(MessageType.EXPENSE.getCode());
            if (payType.intValue() == PayDistributionType.member.getCode().intValue()) {
                message.setDetail("您在面对面扫码支付中使用ep折扣优惠券，消费了" + userEp + "EP！");
            } else if (payType.intValue() == PayDistributionType.store.getCode().intValue()) {
                message.setDetail("您在商家二维码扫码支付中使用ep折扣优惠券，消费了" + userEp + "EP！");
            }
            message.setRemark(MessageType.EXPENSE.getMsg());
            messageService.add(message);

            // 修改系统的ep值
            userService.updateEp("system", userEp);
            //增加一条赠送ep到系统流水记录
            WalletRecord sysRecord = new WalletRecord();
            sysRecord.setOrderNo(walletRecharge.getOrderNo());
            sysRecord.setScore(userEp);
            sysRecord.setUserId("system");
            sysRecord.setRecordType(RecordType.USER_DISCOUNT_EP.getCode());
            if (payType.intValue() == PayDistributionType.member.getCode().intValue()) {
                sysRecord.setRemark("用户" + user.getUid() + "在面对面扫码支付中使用ep折扣券,消费" + userEp + " EP");
            } else if (payType.intValue() == PayDistributionType.store.getCode().intValue()) {
                sysRecord.setRemark("用户" + user.getUid() + "在商家二维码扫码支付中使用ep折扣券,消费" + userEp + " EP");
            }
            walletRecordService.add(sysRecord);
        }
    }


    /**
     * 新增扫码ep消息、支付消息
     **/
    private void addUserScoreAndEpMessage(String id, String orderNo, String msg, Integer type, String detail, String remark) throws Exception {
        Message message = new Message();
        message.setUserId(id);
        message.setOrderNo(orderNo);
        message.setTitle(msg);
        message.setType(type);
        message.setDetail(detail);
        message.setRemark(remark);
        messageService.add(message);
    }


    /**
     * 新增用户流水消息
     **/
    private void addUserScoreRecord(String id, String orderNo, double score, Integer recordType, String msg) throws Exception {
        WalletRecord record = new WalletRecord();
        record.setUserId(id);
        record.setOrderNo(orderNo);
        record.setScore(score);
        record.setRecordType(recordType);
        record.setRemark(msg);
        walletRecordService.add(record);
    }

    /**
     * 新增加入合伙人记录
     **/
    private void addWalletSign(User user,double daily) throws Exception {
        /*记录签到流水*/
        WalletSign walletSign = new WalletSign();
        walletSign.setUserId(user.getId());
        walletSign.setDonateUserId(user.getId());
        walletSign.setDonateUid(user.getUid());
        walletSign.setScore(daily);
        walletSign.setConfirmScore(daily);
        walletSign.setStatus(StatusType.TRUE.getCode());
        walletSign.setSignNo(0);
        walletSign.setGrade(GradeType.grade2.getCode());
        walletSign.setSignCount(0);
        walletSign.setType(SignType.JOIN_VIP_EP_TO_DOU.getCode());
        walletSign.setRemark(SignType.JOIN_VIP_EP_TO_DOU.getMsg());
        walletSign.setDoudou(daily);
        walletSignService.add(walletSign);
    }

    public double dealDistribution(User buyer, WalletRecharge model) throws Exception {
        // 计算分销
        Double firstScore = 0d;
        Double secondScore = 0d;
        Double thirdScore = 0d;
        Double ep = 0d;
        ep = model.getBusinessSendEp();
        if (model.getFirstReferrerScale() > 0d && StringUtils.isNotBlank(buyer.getFirstReferrer())) {
            firstScore = PoundageUtil.getPoundage(model.getConfirmScore() * model.getFirstReferrerScale() / 100d, 1d);
            addAwardScore(firstScore, buyer.getFirstReferrer(), model.getOrderNo());
        }
        if (model.getSecondReferrerScale() > 0d && StringUtils.isNotBlank(buyer.getSecondReferrer())) {
            secondScore = PoundageUtil.getPoundage(model.getConfirmScore() * model.getSecondReferrerScale() / 100d, 1d);
            addAwardScore(secondScore, buyer.getSecondReferrer(), model.getOrderNo());
        }
        if (model.getThirdReferrerScale() > 0d && StringUtils.isNotBlank(buyer.getThirdReferrer())) {
            thirdScore = PoundageUtil.getPoundage(model.getConfirmScore() * model.getThirdReferrerScale() / 100d, 1d);
            addAwardScore(thirdScore, buyer.getThirdReferrer(), model.getOrderNo());
        }
        if (ep > 0) {
            ep = PoundageUtil.getPoundage(model.getConfirmScore() * model.getBusinessSendEp() / 100d, 1d);
        }
        // 计算商家积分
        Double score = firstScore + secondScore + thirdScore + ep;
        return score;
    }


    /**
     * 处理合伙人分销
     *
     * @param buyer
     * @param model
     * @return
     * @throws Exception
     */
    public double dealJoinDistribution(User buyer, WalletRecharge model) throws Exception {
        SysSetting setting = sysSettingService.get();
        ParamUtil util = ParamUtil.getIstance();
        // 计算分销
        Double firstScore = 0d;
        Double secondScore = 0d;
        Double thirdScore = 0d;
        if (model.getFirstReferrerScale() > 0d && StringUtils.isNotBlank(buyer.getFirstReferrer())) {
            double joinFirstReferrScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.JOINFIRSTREFERRERSCALE), 0d),100,4);
            firstScore = PoundageUtil.getPoundage(model.getConfirmScore() * joinFirstReferrScale, 1d);
            addAwardScore(firstScore, buyer.getFirstReferrer(), model.getOrderNo());
        }
        if (model.getSecondReferrerScale() > 0d && StringUtils.isNotBlank(buyer.getSecondReferrer())) {
            double secondReferrerScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.JOINSECONDREFERRERSCALE), 0d),100,4);
            secondScore = PoundageUtil.getPoundage(model.getConfirmScore() * secondReferrerScale, 1d);
            addAwardScore(secondScore, buyer.getSecondReferrer(), model.getOrderNo());
        }
        if (model.getThirdReferrerScale() > 0d && StringUtils.isNotBlank(buyer.getThirdReferrer())) {
            double joinThirdReferrerScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.JOINTHIRDREFERRERSCALE), 0d),100,4);
            thirdScore = PoundageUtil.getPoundage(model.getConfirmScore() * joinThirdReferrerScale, 1d);
            addAwardScore(thirdScore, buyer.getThirdReferrer(), model.getOrderNo());
        }
        // 计算商家积分
        Double score = firstScore + secondScore + thirdScore;
        return score;
    }

    private void addAwardScore(double score, String userId, String orderNo) throws Exception {
        User user = userService.getById(userId);
        if (user == null) {
            return;
        }
        if (score <= 0) {
            return;
        }
        userService.updateScore(userId, score);
        // 增加积分流水
        addUserScoreRecord(userId, orderNo, score, RecordType.AWARD.getCode(), RecordType.AWARD.getMsg());
    }

    /**
     * 商家二维码：通用业务处理
     */
    @Override
    @Transactional
    public Boolean storeScanCodeHandler(User user, String orderNo) throws Exception {
        WalletRecharge model = this.getUserOrderByOrderNo(orderNo);//抓取预付订单
        if (user != null) {
            user = userService.getById(user.getId());
        }
        boolean flag = null == model || model.getStatus().intValue() != RechargeType.PENDING.getCode().intValue();
        if (flag) {
            logger.error(String.format("Illegal recharge orderNo[%s]", orderNo));
            throw new IllegalArgumentException();
        }
        PayDistribution payDistribution = new PayDistribution();
        //余额支付
        if (null != model && model.getSource().intValue() == BankCardType.STORE_SCAN_CODE_BALANCE.getCode()) {
            if (user.getScore() == null || user.getScore() < model.getConfirmScore()) {
                throw new RuntimeException("用户余额不足");
            }
            //扣减用户积分
            userService.updateScore(user.getId(), -model.getConfirmScore());
        }
        //获赠EP的用户
        String receviceUserId = "";
        //支付宝发起的扫码支付，没有登录账号，所以不知道购买者是谁
        if (null != user) {
            if(model.getConfirmScore() != 0){
                //扣减用户积分流水
                addUserScoreRecord(user.getId(), model.getOrderNo(), -model.getConfirmScore(), RecordType.STORE_SCANCODE_USER.getCode(), RecordType.STORE_SCANCODE_USER.getMsg());
                //扣减用户积分消息
                String userDetail = "扫码支付成功，支出金额" + model.getConfirmScore();
                addUserScoreAndEpMessage(user.getId(), model.getOrderNo(), MessageType.STORE_SCANCODE_USER.getMsg(), MessageType.STORE_SCANCODE_USER.getCode(), userDetail, MessageType.STORE_SCANCODE_USER.getMsg());
            }
            receviceUserId = user.getId();
            //增加用户消费ep流水 EP折扣处理
            this.addSystemEp(user, orderNo, PayDistributionType.store.getCode());
        } else {
            //三级分销以商家为结算对象往上3层进行（一级、二级、三级）结算
            user = userService.getById(model.getStoreUserId());
            //赠送EP对象为系统
            receviceUserId = this.SYSTEM;
        }

        //判断用户是否全额支付
        if (model.getConfirmScore() != 0) {
            //三级分销业务
            double sumScore = this.dealDistribution(user, model);

            BigDecimal bd1 = new BigDecimal(Double.toString(model.getConfirmScore()));
            BigDecimal bd2 = new BigDecimal(Double.toString(sumScore));
            model.setConfirmScore(bd1.subtract(bd2).doubleValue());
            //系统抽取费用
            Double tradeScore = userService.tradeRate(model.getOrderNo(), model.getStoreUserId(), model.getConfirmScore());
            //商家所得积分=总交易积分-三级分销-赠送EP-系统抽取费用
            Double score = model.getConfirmScore() - tradeScore;
            score = PoundageUtil.getPoundage(score, 1d);
            model.setConfirmScore(score);
            //增加商户积分
            userService.updateScore(model.getStoreUserId(), model.getConfirmScore());
            //增加商户积分流水
            addUserScoreRecord(model.getStoreUserId(), model.getOrderNo(), model.getConfirmScore(), RecordType.STORE_SCANCODE_STORE.getCode(), RecordType.STORE_SCANCODE_STORE.getMsg());
            //增加商户积分消息
            String storeDetail = "用户扫码支付成功，收到" + model.getConfirmScore() + "金额";
            addUserScoreAndEpMessage(model.getStoreUserId(), model.getOrderNo(), MessageType.STORE_SCANCODE_STORE.getMsg(), MessageType.STORE_SCANCODE_STORE.getCode(), storeDetail, MessageType.STORE_SCANCODE_STORE.getMsg());
        }

        if (model.getBusinessSendEp() != null && model.getBusinessSendEp() > 0) {
            Double ep = PoundageUtil.getPoundage(model.getScore() * model.getBusinessSendEp() / 100d, 1d);
            if (ep > 0) {
                userService.updateEpById(receviceUserId, ep);
                //增加用户ep流水
                addUserScoreRecord(receviceUserId, model.getOrderNo(), ep, RecordType.STORE_SCANCODE_EP.getCode(), RecordType.STORE_SCANCODE_EP.getMsg());
                //增加EP流水记录
                User sendUser = userService.getById(receviceUserId);
                epRecordService.consumeEpRecord(userService.getById(model.getStoreUserId()),-ep,model.getOrderNo(),EPRecordType.STORE_SCANCODE_EP,model.getStoreUserId(),receviceUserId,"");
                epRecordService.consumeEpRecord(user,ep,model.getOrderNo(),EPRecordType.STORE_SCANCODE_EP,receviceUserId,model.getStoreUserId(),"");
                //增加用户ep消息
                String detail = "扫码支付成功，获赠" + ep + "EP";
                addUserScoreAndEpMessage(receviceUserId, model.getOrderNo(), MessageType.STORE_SCANCODE_EP.getMsg(), MessageType.SYSTEM.getCode(), detail, MessageType.STORE_SCANCODE_EP.getMsg());

                //新增赠送与ep等额的斗斗给会员的记录
               /* WalletRecord doudouRecord = new WalletRecord();
                doudouRecord.setUserId(user.getId());
                doudouRecord.setOrderNo(model.getOrderNo());
                doudouRecord.setScore(model.getDoudou());
                doudouRecord.setRecordType(RecordType.STORE_SCANCODE_DOUDOU.getCode());
                doudouRecord.setRemark(RecordType.STORE_SCANCODE_DOUDOU.getMsg());
                walletRecordService.add(doudouRecord);*/

                //新增用户收到斗斗的记录
                /*WalletSign walletSign = new WalletSign();
                walletSign.setUserId(user.getId());
                walletSign.setDonateUserId(user.getId());
                walletSign.setDonateUid(user.getUid());
                walletSign.setDoudou(model.getDoudou());
                walletSign.setOrderNo(model.getOrderNo());
                walletSign.setScale(0d);
                walletSign.setType(SignType.STORE_SCANCODE_DOUDOU.getCode());
                walletSign.setRemark(SignType.STORE_SCANCODE_DOUDOU.getMsg());
                walletSign.setScore(model.getDoudou());
                walletSign.setConfirmScore(model.getDoudou());
                walletSign.setGrade(user.getGrade());
                walletSign.setStatus(StatusType.TRUE.getCode());
                walletSignService.add(walletSign);*/

                //增加用户的斗斗值
//                userService.updateDoudou(user.getId(),model.getDoudou());

                //无ep折扣优惠,创建ep业绩赠送明细
                User storeUser = userService.getById(model.getStoreUserId());
                partnerBillDetailService.createPartnerBillDetail(storeUser.getStoreId(),model.getOrderNo(),model.getBusinessSendEp());
            }
        }
        // 修改支付订单
        WalletRecharge updateModel = new WalletRecharge();
        updateModel.setStatus(RechargeType.TRANSFER_SUCCESS.getCode());
        this.update(model.getId(), updateModel);
        //用户扫码支付成功推送
        if (!"system".equalsIgnoreCase(receviceUserId)) {
            JPushUtil.pushPayloadByid(user.getRegistrationId(), "您有一笔金额为：" + model.getScore() + "的交易", new JpushExtraModel(JpushExtraModel.NOTIFIYPE, JpushExtraModel.EXPENSE_MSM));
        }
        //商家扫码支付成功推送
        User store = userService.getById(model.getStoreUserId());
        JPushUtil.pushPayloadByid(store.getRegistrationId(), "您有一笔金额为：" + model.getScore() + "的交易", new JpushExtraModel(JpushExtraModel.NOTIFIYPE, JpushExtraModel.EXPENSE_MSM));
        //异步处理60层销售业绩业务
        //第一次加入合伙人，向上累加EP销售业绩,异步处理
       /* final String userId = user.getId();
        final User user1 = user;
        final String order1 = orderNo;
        new Thread(new Runnable() {
            public void run() {
                try {
                    //客户端微信注册登录，用户根据系统算法分组
                    userService.updatePermanceByThread(user1,order1,0d);
                } catch (Exception e) {
                    logger.error("处理分组失败！！" + e.getMessage());
                }
            }
        }).start();*/
        return true;
    }

    /**
     * 加入合伙人支付 1680
     *
     * @param user    支付用户
     * @param orderNo 订单号
     * @return
     * @throws Exception
     */

    @Override
    @Transactional
    public Boolean joinPartnerHandler(User user, String orderNo) throws Exception {
        WalletRecharge model = this.getUserOrderByOrderNo(orderNo);//抓取预付订单
        SysSetting sysSetting = sysSettingService.get();
        if (user != null) {
            user = userService.getById(user.getId());
        }
        boolean flag = null == model || model.getStatus().intValue() != RechargeType.PENDING.getCode().intValue();
        if (flag) {
            logger.error(String.format("Illegal recharge orderNo[%s]", orderNo));
            throw new IllegalArgumentException();
        }
        //获取支付的三级分销比例和ep比例
        ParamUtil util = ParamUtil.getIstance();
        if (util == null) {
            return false;
        }
        Double joinEp = ToolUtil.parseDouble(util.get(Parameter.JOINEP), 0d);
        Double joinRmbScale = ToolUtil.parseDouble(util.get(Parameter.JOINRMBSCALE), 0d);
        Double exchangeEp = user.getExchangeEP();
        Double needEP = joinEp - joinEp * joinRmbScale;
        Double realMoney = 0d;
        if (model.getDiscountEP()!=null && model.getDiscountEP()>0){
            if (user.getExchangeEP() >= needEP) {
                //EP足额，以EP为主
                realMoney=PoundageUtil.getPoundage(joinEp * joinRmbScale,1d,2);
            }else  if (exchangeEp < needEP && exchangeEp>0){
                needEP=exchangeEp;
                realMoney=PoundageUtil.getPoundage(joinEp-needEP,1d,2);
            }else {
                realMoney=joinEp;
                needEP=0d;
            }
        }else {
            realMoney=joinEp;
            needEP=0d;
        }
        model.setDiscountEP(needEP);
        model.setConfirmScore(realMoney);
        //余额支付
        if (null != model && model.getSource().intValue() == BankCardType.JOIN_BALANCE.getCode()) {
            if (user.getScore() == null || user.getScore() < model.getConfirmScore()) {
                throw new RuntimeException("用户余额不足");
            }
            //扣减用户积分
            userService.updateScore(user.getId(), -model.getConfirmScore());
        }
        if (model.getDiscountEP()>0){
            //有使用EP抵扣,扣减用户EP
            userService.updateEpById(user.getId(),-model.getDiscountEP());
            //增加用户EP流水 加入EP业绩ep消费统计
            epRecordService.consumeEpRecord(user,-model.getDiscountEP(),orderNo, EPRecordType.JOIN_PARTNER,user.getId(),Constant.SYSTEM_USERID,"");
            //增加用户流水总表EP流水
            addUserScoreRecord(user.getId(), model.getOrderNo(), model.getDiscountEP(), RecordType.JOIN_PAY.getCode(), RecordType.JOIN_PAY.getMsg());
            //增加系统EP消息
            String storeDetail = "用户" + user.getUid() + "加入合伙人，现金支付"+PoundageUtil.getPoundage(model.getScore()-model.getDiscountEP(),1d,2)+",EP支付" + model.getDiscountEP();
            addUserScoreAndEpMessage(user.getId(), model.getOrderNo(), MessageType.JOIN_PAY.getMsg(), MessageType.JOIN_PAY.getCode(), storeDetail, MessageType.JOIN_PAY.getMsg());
        }
        //扣减用户积分流水
        addUserScoreRecord(user.getId(), model.getOrderNo(), -model.getConfirmScore(), RecordType.JOIN_PAY.getCode(), RecordType.JOIN_PAY.getMsg());
        //扣减用户积分消息
        String userDetail = "加入合伙人，支出金额" + model.getConfirmScore();
        addUserScoreAndEpMessage(user.getId(), model.getOrderNo(), MessageType.JOIN_PAY.getMsg(), MessageType.JOIN_PAY.getCode(), userDetail, MessageType.JOIN_PAY.getMsg());
        //处理分销
        double sumScore = this.dealJoinDistribution(user, model);

        BigDecimal bd1 = new BigDecimal(Double.toString(model.getConfirmScore()));
        BigDecimal bd2 = new BigDecimal(Double.toString(sumScore));
        model.setConfirmScore(bd1.subtract(bd2).doubleValue());
        //系统所得积分=总交易积分-三级分销-赠送EP
        Double score = model.getConfirmScore();
        score = PoundageUtil.getPoundage(score, 1d);
        model.setConfirmScore(score);
        //增加系统EP
        userService.updateEpById(SYSTEM, model.getConfirmScore());
        //增加系统EP流水
        addUserScoreRecord(SYSTEM, model.getOrderNo(), model.getConfirmScore(), RecordType.JOIN_PAY.getCode(), RecordType.JOIN_PAY.getMsg());
        //增加系统EP消息
        String storeDetail = "用户" + user.getUid() + "加入合伙人，系统收到" + model.getConfirmScore() + "EP";
        addUserScoreAndEpMessage(SYSTEM, model.getOrderNo(), MessageType.JOIN_PAY.getMsg(), MessageType.JOIN_PAY.getCode(), storeDetail, MessageType.JOIN_PAY.getMsg());
        //增加EP流水记录
        //epRecordService.consumeEpRecord(user,-800d,model.getOrderNo(), EPRecordType.JOIN_PARTNER);
        //更改用户等级
        User updateUser = new User();
        updateUser.setId(user.getId());
        //第一次加入合伙人等级为VIP
        updateUser.setGrade(GradeType.grade2.getCode());
        updateUser.setUpdateTime(new Date());
        userService.update(user.getId(), updateUser);
        //加入合伙人成功后，800EP兑换成斗斗给用户,以800为基数，乘以管理后台配置的倍数，动态赠送
        double joinTodouScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.JOINTODOUSCALE), 0d);
        userService.updateDoudou(user.getId(), PoundageUtil.getPoundage(800 * joinTodouScale, 1d));
        //增加加入合伙人记录，同时也是加入合伙人时候的赠送斗斗记录
        addWalletSign(user, PoundageUtil.getPoundage(800 * joinTodouScale, 1d));
        //第一次加入合伙人，向上累加EP销售业绩，V4.4之后，销售业绩由后台配置，往上加60层
        Double joinPerformance = ToolUtil.parseDouble(util.get(Parameter.JOINPERFORMANCE), 800d);
        userService.updatePerformanceCount(user.getId(), joinPerformance); // 此处是同步处理，现改为下面的线程池异步处理
        //第一次加入合伙人，清除用户的所有销售业绩
        gradeService.clearAllPerformance(user,GradeType.grade2.getCode());
        //新增业绩处理记录
        performanceRecordService.create(orderNo,joinPerformance,user.getId(),model.getSource(),model.getRemark());
        //插入提现记录，用户实付金额需要给对应地区的区域省代 区代 市代做业绩提成，此处为了简化，直接当作提现订单来处理
        // 增加兑换记录
        WalletExchange exchange = new WalletExchange();
        exchange.setUserId(user.getId());
        exchange.setScore(-realMoney);
        exchange.setPoundage(0d);
        exchange.setConfirmScore(realMoney);
        exchange.setBankName("加入合伙人现金部分给代理提成");
        exchange.setBankId("");
        exchange.setCardType(JOIN_DONATE_FOR_AGENT.getCode());
        exchange.setRemark("加入合伙人现金部分给代理提成");
        exchange.setCardNo("");
        walletExchangeService.add(exchange);
        // 修改支付订单
        WalletRecharge updateModel = new WalletRecharge();
        updateModel.setStatus(RechargeType.TRANSFER_SUCCESS.getCode());
        this.update(model.getId(), updateModel);
//        epRecordService.consumeEpRecord(user,-800d,orderNo, EPRecordType.JOIN_SCAN_EP,user.getId(),Constant.SYSTEM_USERID,"");
        //用户支付成功推送
        if (StringUtils.isNotEmpty(user.getRegistrationId())) {
            JPushUtil.pushPayloadByid(user.getRegistrationId(), "您有一笔金额为：" + model.getScore() + "的交易", new JpushExtraModel(JpushExtraModel.NOTIFIYPE, JpushExtraModel.EXPENSE_MSM));
        }
        return true;
    }

    @Override
    public PageResult<WalletRecharge> getByscore(WalletRecharge wr, List<Integer> sources, Page page) throws Exception {

        PageResult<WalletRecharge> pageResult = new PageResult<WalletRecharge>();
        BeanUtils.copyProperties(pageResult, page);

        Integer count = walletRechargeDao.countByscore(wr, sources);
        pageResult.setTotalSize(count);
        if (count != null && count > 0) {
            List<WalletRecharge> list = walletRechargeDao.getByscore(wr, sources, page);
            pageResult.setRows(list);
        }
        return pageResult;
    }


    @Override
    public Double statistics(WalletRecharge wr, List<Integer> sources,
                             String nowDate) throws Exception {
        return walletRechargeDao.statistics(wr, sources, nowDate);
    }

    /**
     * 扫码支付计算金额
     *
     * @param storeUserId
     * @param payType     1:面对面  2 ：商家二维码
     * @param Money
     * @return
     * @throws Exception
     */
    @Transactional
    public Map<String, Double> countWalletMoney(User user, String storeUserId, Integer payType, Double Money) throws Exception {

        //定义一个Map集合保存数据
        Map<String, Double> map = new HashMap<>();

        //根据用户id和支付类型查询付款分销
        PayDistribution payDistribution = payDistributionService.getByUserId(storeUserId, payType);

        //保存ep折扣优惠
        Double totalDiscountEP = 0d;
        //实付金额
        Double countMoney = 0d;

        //计算付款金额
        totalDiscountEP = PoundageUtil.getPoundage(payDistribution.getDiscountEP() * Money / 100d, 1d);

        //判断用户ep是否足够支付
        if (user.getExchangeEP() < totalDiscountEP) {
            //如果不足，则获取用户所有ep
            totalDiscountEP = user.getExchangeEP();
        }

        //计算实付金额
        countMoney = Money - totalDiscountEP;
        countMoney = PoundageUtil.getPoundage(countMoney, 1d);
        map.put("totalMoney", PoundageUtil.getPoundage(Money, 1d));
        map.put("totalDiscountEP", PoundageUtil.getPoundage(totalDiscountEP, 1d));
        map.put("countMoney", countMoney);

        return map;
    }
}
