package com.yanbao.service.impl;

import com.mall.model.*;
import com.yanbao.constant.*;
import com.yanbao.core.model.JpushExtraModel;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.dao.UserDao;
import com.yanbao.service.*;
import com.yanbao.util.*;
import com.yanbao.vo.OrderVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final Integer SYSTEM_USER_UID = 200000;

    private static final String SYSTEM_USER_USERID = "system";

    // private static final Integer GROUP_COUNT_LEVEL = 60;

    @Autowired
    private UserDao userDao;
    @Autowired
    private ConsumeEPRecordService consumeEPecordService;
    @Autowired
    private WalletRechargeService walletRechargeService;

    @Autowired
    private StoreService storeService;
    @Autowired
    private GoodsWinService goodsWinService;
    @Autowired
    private WalletRecordService walletRecordService;
    @Autowired
    private BusinessEpRecordService businessEpRecordService;
    @Autowired
    private GoodsIssueService goodsIssueService;
    @Autowired
    private WalletSysRecordService walletSysRecordService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private GoodsWinDetailService goodsWinDetailService;
    @Autowired
    private EPRecordService epRecordService;
    @Autowired
    private WalletSignService walletSignService;
    @Autowired
    private PerformanceRecordService performanceRecordService;
    @Autowired
    private PartnerBillDetailService partnerBillDetailService;


    @Override
    public User getById(String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format("Get user info by id[%s]", id));
        }
        return userDao.getById(id);
    }

    @Override
    public User getByCondition(User model) throws Exception {
        if (null == model) {
            return null;
        }
        return userDao.getByCondition(model);
    }

    @Override
    public Integer count(User condition) throws Exception {
        if (null == condition) {
            return null;
        }
        return userDao.count(condition);
    }

    @Override
    public PageResult<User> getPage(User condition, Page page) throws Exception {
        if (null == condition) {
            return null;
        }
        PageResult<User> pageResult = new PageResult<User>();
        BeanUtils.copyProperties(pageResult, page);

        Integer count = userDao.count(condition);
        pageResult.setTotalSize(count);
        if (count != null && count > 0) {
            List<User> list = userDao.getList(condition, page);
            pageResult.setRows(list);
        }
        return pageResult;
    }

    @Override
    public Integer add(User user) throws Exception {
        if (user == null) {
            return null;
        }
        setDefaultValue(user);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("add user[%s]", user.toString()));
        }
        return userDao.add(user);
    }

    @Override
    public Integer update(String id, User model) throws Exception {
        if (StringUtils.isBlank(id) || model == null) {
            return 0;
        }
        model.setUpdateTime(new Date());
        return userDao.update(id, model);
    }

    @Override
    public Integer updateBySignIn(String id, User model) throws Exception {
        if (StringUtils.isBlank(id) || model == null) {
            return 0;
        }
        model.setUpdateTime(new Date());
        return userDao.updateBySignIn(id, model);
    }

    private void setDefaultValue(User user) {
        String salt = DateTimeUtil.formatDate(new Date(), DateTimeUtil.PATTERN_B);
        user.setId(UUIDUtil.getUUID());
        user.setSalt(salt);
        user.setScore(0d);
        if (StringUtils.isNotBlank(user.getPassword())) {
            user.setPassword(Md5Util.MD5Encode(user.getPassword().trim(), salt));
        }
        if (StringUtils.isNotBlank(user.getPayPwd())) {
            user.setPayPwd(Md5Util.MD5Encode(user.getPayPwd().trim(), salt));
        }
        if (StringUtils.isEmpty(user.getNickName())){
            user.setNickName("http://user.doupaimall.com/logo.png");
        }
        user.setStatus(StatusType.TRUE.getCode());
        user.setIsKF(StatusType.FALSE.getCode());
        user.setGroupChildCountA(0);
        user.setGroupChildCountB(0);
        user.setGroupChildCountC(0);
        user.setCreateTime(new Date());
        user.setExchangeEP(0d);
        user.setConsumeEP(0d);
        user.setGrade(0);
        user.setRemainSign(0);
        user.setPerformanceOne(0d);
        user.setPerformanceTwo(0d);
        user.setPerformanceThree(0d);
    }

    @Override
    public Integer updateScore(String id, Double score) throws Exception {
        if (StringUtils.isBlank(id) || score == null || score == 0) {
            return 0;
        }
        return userDao.updateScore(id, score);
    }

    @Override
    public Integer updateDoudou(String id, Double doudou) throws Exception {
        if (StringUtils.isBlank(id) || doudou == null || doudou == 0) {
            return 0;
        }
        return userDao.updateDoudou(id, doudou, null);
    }

    @Override
    public Integer updateDoudou(String id, Double doudou, Date douSignTime) throws Exception {
        if (StringUtils.isBlank(id) || doudou == null || doudou == 0) {
            return 0;
        }
        return userDao.updateDoudou(id, doudou, douSignTime);
    }

    @Override
    public Integer updateEp(String id, Double exchangeEP) throws Exception {
        if (StringUtils.isBlank(id) || exchangeEP == null || exchangeEP == 0) {
            return 0;
        }
        return userDao.updateEp(id, exchangeEP);
    }

    @Override
    public void updateGroupCount(String id, String groupType) throws Exception {
        User user = getById(id);
        User referrer = getById(user.getFirstReferrer());
        // 设置用户分组
        if (StringUtils.isBlank(groupType)) {
            user.setGroupType(this.setUserGroupType(user, referrer));// 系统根据算法进行分组
        } else if (groupType.equalsIgnoreCase("A") || groupType.equalsIgnoreCase("B") || groupType.equalsIgnoreCase("C")) {
            user.setGroupType(groupType);// 指定分组
            User updateGroupType = new User();
            updateGroupType.setGroupType(groupType);
            this.update(user.getId(), updateGroupType);
        }
        // 获取统计层数
        int levelNo = ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.LEVELNO),0);
        // 更新分组统计
        for (int i = 0; i < levelNo; i++) {
            if (referrer == null || referrer.getUid() < SYSTEM_USER_UID) {
                break;
            }
            if ("A".equals(user.getGroupType())) {
                userDao.updateGroupChildCountA(referrer.getId());
            } else if ("B".equals(user.getGroupType())) {
                userDao.updateGroupChildCountB(referrer.getId());
            } else if ("C".equals(user.getGroupType())) {
                userDao.updateGroupChildCountC(referrer.getId());
            } else {
                break;
            }
            user = referrer;
            referrer = this.getById(user.getFirstReferrer());
        }
    }

    private String setUserGroupType(User user, User referrer) throws Exception {
        // 只计算1度人脉分组情况
        String groupType = userDao.getMinGroupType(referrer.getId());
        if (groupType == null || "".equals(groupType)) {
            groupType = "A";
        }
        User updateGroupType = new User();
        updateGroupType.setGroupType(groupType);
        this.update(user.getId(), updateGroupType);
        return groupType;
    }

    @Override
    public Integer updateRegistrationId(String id, String registrationId) throws Exception {
        Integer rs = userDao.updateRegistrationId(id, registrationId);
        return rs;
    }

    @Override
    public Integer updateEpById(String id, Double ep) throws Exception {
        return userDao.updateEpById(id, ep);
    }

    @Override
    public Integer updateConsumeEPById(String id, Double consumeEP) throws Exception {
        return userDao.updateConsumeEPById(id, consumeEP);
    }

    @Override
    @Transactional
    public JsonResult join(JsonResult jsonResult, User user, Double ep) throws Exception {
        ConsumeEPRecord model = new ConsumeEPRecord();
        model.setId(UUIDUtil.getUUID());
        model.setCreateTime(new Date());
        model.setUpdateTime(new Date());
        model.setConsumeEp(ep);
        model.setStatus(1);
        model.setUserId(user.getId());
        model.setGrade(user.getGrade());
        model.setRemark("加入合伙人扣取800EP,晋级为" + GradeType.grade2.getMsg());
        model.setType(ConsumeEPType.JOIN_PARTNER.getCode());
        consumeEPecordService.add(model);
        //增加EP流水记录
        epRecordService.consumeEpRecord(user, -800d, model.getOrderNo(), EPRecordType.JOIN_PARTNER,user.getId(),Constant.SYSTEM_USERID,"");
        // 增加积分流水
        WalletRecord record = new WalletRecord();
        record.setUserId(user.getId());
        record.setOrderNo(model.getOrderNo());
        record.setScore(ep);
        record.setRecordType(RecordType.CONSUMEEP.getCode());
        record.setRemark(RecordType.CONSUMEEP.getMsg());
        walletRecordService.add(record);
        this.updateEpById(user.getId(), -ep);
        this.updateEpById(SYSTEM_USER_USERID, ep);
        User m = new User();
        //默认等级为VIP
        m.setGrade(GradeType.grade2.getCode());
        m.setUpdateTime(new Date());
        userDao.update(user.getId(), m);
        //加入合伙人成功后，800EP兑换成斗斗给用户
        double joinTodouScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.JOINTODOUSCALE),0d);
        this.updateDoudou(user.getId(), PoundageUtil.getPoundage(800 * joinTodouScale, 1d));
        //增加加入合伙人记录
        WalletSign walletSign = this.addWalletSign(user, PoundageUtil.getPoundage(800 * joinTodouScale, 1d));
        jsonResult.setMsg("成功加入合伙人,晋级为" + GradeType.grade2.getMsg());
        //第一次加入合伙人，向上累加EP销售业绩
        this.updatePerformanceCount(user.getId(), 800d);
        //新增业绩处理记录
        performanceRecordService.create(walletSign.getOrderNo(),800d,user.getId(),null,"扣除用户800EP加入合伙人");
        //清算业绩
        gradeService.clearAllPerformance(user, GradeType.grade2.getCode());
        return jsonResult;
    }

    @Override
    @Transactional
    public void updatePerformanceCount(String id, Double count) throws Exception {
        User user = getById(id);
        User referrer = getById(user.getFirstReferrer());
        // 获取向上统计层数
        List<String> listA = new ArrayList<String>();
        List<String> listB = new ArrayList<String>();
        List<String> listC = new ArrayList<String>();
        // 更新分组统计
        int levelNo = ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.LEVELNO),0);
        for (int i = 0; i < levelNo; i++) {
            if (referrer == null || referrer.getUid() < SYSTEM_USER_UID) {
                break;
            }
            if ("A".equals(user.getGroupType())) {
                listA.add(referrer.getId());
            } else if ("B".equals(user.getGroupType())) {
                listB.add(referrer.getId());
            } else if ("C".equals(user.getGroupType())) {
                listC.add(referrer.getId());
            } else {
                break;
            }
            user = referrer;
            referrer = this.getById(user.getFirstReferrer());
        }
        if(listA.size() > 0){
            userDao.batchUpdatePerformanceOne(listA,count);
        }
        if(listB.size() > 0){
            userDao.batchUpdatePerformanceTwo(listB,count);
        }
        if(listC.size() > 0){
            userDao.batchUpdatePerformanceThree(listC,count);
        }
    }

    /**
     * 商家发货接口
     */
    @Override
    @Transactional
    public JsonResult sendGoodService(OrderVo vo, User user, GoodsWin order) throws Exception {

        order.setExpressNo(vo.getExpressNo());
        order.setExpressName(vo.getExpressName());
        order.setStatus(GoodsWinType.DELIVERED.getCode());
        goodsWinService.update(order.getId(), order);
        //发送消息提醒用户商品已发货
        addSystemMessage(order);
        // 直接购买的商品，计算分销
        boolean flag = order.getOrderType().intValue() == OrderType.PURCHASE.getCode().intValue() || order.getOrderType().intValue() == OrderType.CARTPAY.getCode().intValue();
        if (flag) {
            Store store = storeService.getById(user.getStoreId());
            User buyer = null;
            //通过分享链接购买的，三级分销应是分享者的id
            if (StringUtils.isNotBlank(order.getShareUserId())) {
                buyer = this.getById(order.getShareUserId());
                String newSecondReferrer = buyer.getFirstReferrer();
                String newThirdReferrer = buyer.getSecondReferrer();
                buyer.setFirstReferrer(order.getShareUserId());
                buyer.setSecondReferrer(newSecondReferrer);
                buyer.setThirdReferrer(newThirdReferrer);
            } else {
                buyer = this.getById(order.getUserId());
            }
            //商品总价
            Double sumPrice = order.getPrice();//多种商品时订单的总价直接等于price
            if (order.getOrderType().intValue() == OrderType.PURCHASE.getCode().intValue()) {
                //原有逻辑和单件商品时，订单总价=单价X数量
                sumPrice = order.getPrice() * order.getNum();
            }
            //判断用户是否根据ep折扣优惠进行购买，如果是，没有分销
            Double score = 0d;
            if (order.getDiscountEP() == null) {
                order.setDiscountEP(0d);
            }
            if (order.getDiscountEP() == 0) {
                //用户没有进行ep折扣优惠购买，有分销
                // 计算分销
                Double sendScore = calcAwardScore(order, buyer);
                // 商家所得积分
                score = sumPrice - sendScore;
            } else {
                //用户通过ep折扣优惠购买，没有分销
                score = sumPrice;
                //扣除ep,增加ep到系统
                addSystemEp(order);
            }

            if (score != 0) {
                //系统抽取费用
                Double tradeScore = tradeRate(order.getOrderNo(), user.getId(), score);
                score = score - tradeScore;
                score = PoundageUtil.getPoundage(score, 1d);
                // 增加商家积分
                this.updateScore(store.getUserId(), score);
                // 增加积分流水
                WalletRecord record = new WalletRecord();
                record.setUserId(store.getUserId());
                record.setOrderNo(order.getOrderNo());
                record.setScore(score);
                record.setRecordType(RecordType.STORE_INCOME.getCode());
                record.setRemark(RecordType.STORE_INCOME.getMsg());
                walletRecordService.add(record);
            }
        } else {//通过斗拍中奖后积分购买的

            Store store = storeService.getById(user.getStoreId());
            Double ep = 0d;
            //中奖的期数信息
            GoodsIssue goodsIssue = goodsIssueService.getById(order.getIssueId());
            // 判断是否有ep赠送
            if (goodsIssue.getBusinessSendEp() > 0d) {
                // 积分购买，赠送ep时减去商家的积分
                ep = goodsIssue.getBusinessSendEp();
            }
            Double score = order.getPrice() - ep;
            //系统抽取费用
            Double tradeScore = tradeRate(order.getOrderNo(), user.getId(), score);
            score = score - tradeScore;
            score = PoundageUtil.getPoundage(score, 1d);
            //增加商家积分
            this.updateScore(store.getUserId(), score);
            // 增加积分流水
            WalletRecord record2 = new WalletRecord();
            record2.setUserId(store.getUserId());
            record2.setOrderNo(order.getOrderNo());
            record2.setScore(score);
            record2.setRecordType(RecordType.STORE_INCOME.getCode());
            record2.setRemark(RecordType.STORE_INCOME.getMsg());
            walletRecordService.add(record2);
        }
        // 获取下单者的对象
        User buyUser = this.getById(order.getUserId());
        //赠送EP:无EP抵扣才会有赠送
        if (order.getDiscountEP() == null || order.getDiscountEP() == 0) {
            sendEp(order);
        }
        logger.debug("基本上没问题");
        // 普通用户购买成功后推送给商家，提醒发货
        boolean rs = JPushUtil.pushPayloadByid(buyUser.getRegistrationId(), "您在[" + order.getStoreName() + "]商家有一笔订单[" + order.getOrderNo() + "]发货啦！", new JpushExtraModel(JpushExtraModel.NOTIFIYPE, JpushExtraModel.USER_ORDER));
        logger.info("推送ID:" + buyUser.getRegistrationId() + ";用户ID:" + buyUser.getUid() + ";推送结果：" + rs);
        return new JsonResult();
    }

    private void addSystemMessage(GoodsWin order) throws Exception {
        // 增加发货消息
        Message message = new Message();
        message.setUserId(order.getUserId());
        message.setOrderNo(order.getOrderNo());
        message.setTitle(MessageType.SHIPPED.getMsg());
        message.setType(MessageType.SHIPPED.getCode());
        message.setDetail("您在[" + order.getStoreName() + "]商家有一笔订单[" + order.getOrderNo() + "]发货啦！");
        message.setRemark(MessageType.SHIPPED.getMsg());
        messageService.add(message);
    }

    private void addEPMessage(GoodsWin order, Double ep) throws Exception {
        // 增加发货消息
        Message message = new Message();
        message.setUserId(order.getUserId());
        message.setOrderNo(order.getOrderNo());
        message.setTitle(MessageType.SENDEP.getMsg());
        message.setType(MessageType.SYSTEM.getCode());
        message.setDetail("您在[" + order.getStoreName() + "]商家有一笔订单[" + order.getOrderNo() + "]成功发货并获取" + ep + "EP！");
        message.setRemark(MessageType.SENDEP.getMsg());
        messageService.add(message);
    }

    private void addAwardScore(double score, String userId, String orderNo) throws Exception {
        User user = this.getById(userId);
        if (user == null) {
            return;
        }
        this.updateScore(userId, score);
        // 增加积分流水
        WalletRecord record = new WalletRecord();
        record.setUserId(userId);
        record.setOrderNo(orderNo);
        record.setScore(score);
        record.setRecordType(RecordType.AWARD.getCode());
        record.setRemark(RecordType.AWARD.getMsg());
        walletRecordService.add(record);
    }

    @Override
    public List<User> getInnerList(List<Integer> uidList) throws Exception {
        return userDao.getInnerList(uidList);
    }

    @Override
    public Integer updateBindEpById(String id, Double ep) throws Exception {
        return userDao.updateBindEpById(id, ep);
    }

    @Override
    public Integer updateBindDoudouById(String id, Double doudou) throws Exception {
        return userDao.updateBindDoudouById(id, doudou);
    }

    @Override
    @Transactional
    public void updateUserPhoneSendEp(User user, String phone, Token token) throws Exception {
        user = this.getById(user.getId());
        /*User systemUser = this.getById(SYSTEM_USER_USERID);
        System.out.println("**********判断系统账户*************");
        boolean ex = systemUser.getExchangeEP() < (setting.getInviterEP() + setting.getRegisterEP());
        System.out.println("**********判断系统账户*************  结果 " + ex);
        if (ex) {
            logger.error(user.getUid() + "绑定手机号，系统ep不足");
            throw new RuntimeException("系统ep不足");
        }*/
        System.out.println("**********111111111111111111111*************");
//		boolean inviter=setting.getInviterEP() > 0 && !org.springframework.util.StringUtils.isEmpty(user.getPhone()) && user.getBindEP() <= setting.getBindEP();
        double inviterDoudou = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.INVITERDOUDOU),0d);
        boolean inviter = inviterDoudou > 0 && !org.springframework.util.StringUtils.isEmpty(user.getPhone());
        logger.error(inviter + "");
        System.out.println("***********************");
        if (inviter) {
            sendInviterDoudou(user, OrderNoUtil.get());
        }
        double registerDoudou = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.REGISTERDOUDOU),0d);
        if (registerDoudou > 0) {
            sendRegisterDoudou(user,OrderNoUtil.get());
        }
    }

    /**
     * @param user
     * @param setting
     * @throws Exception
     */
    private void senInviterEp(User user, SysSetting setting) throws Exception {
        Boolean flag= this.isCompleteInfo(user);
        if (!flag){
            return;
        }
        User inviter = this.getById(user.getFirstReferrer());
        if (inviter.getBindEP() == null) {
            inviter.setBindEP(0d);
        }
        //vip && 累计赠送EP小于系统设置才赠送
        if (inviter.getGrade() < 1 || inviter.getBindEP() >= setting.getBindEP()) {
            return;
        }
        //增加EP流水表记录
        epRecordService.consumeEpRecord(user, setting.getInviterEP(), OrderNoUtil.get(), EPRecordType.BIND_PHONE_RECOMMEND_SENDEP,Constant.SYSTEM_USERID,user.getId(),"");
        this.updateEpById(inviter.getId(), setting.getInviterEP());
        this.updateBindEpById(inviter.getId(), setting.getInviterEP());
        WalletRecord inviterRecord = new WalletRecord();
        inviterRecord.setOrderNo(OrderNoUtil.get());
        inviterRecord.setScore(setting.getInviterEP());
        inviterRecord.setUserId(inviter.getId());
        inviterRecord.setRecordType(RecordType.INVITEREP.getCode());
        inviterRecord.setRemark("用户" + inviter.getUid() + "邀请用户" + user.getUid() + "注册并完善资料，获赠" + setting.getInviterEP() + "EP");
        walletRecordService.add(inviterRecord);
        this.updateEpById(SYSTEM_USER_USERID, -setting.getInviterEP());
        WalletRecord inviterSysRecord = new WalletRecord();
        inviterSysRecord.setOrderNo(OrderNoUtil.get());
        inviterSysRecord.setScore(-setting.getInviterEP());
        inviterSysRecord.setUserId(SYSTEM_USER_USERID);
        inviterSysRecord.setRecordType(RecordType.SYSBINDPHONEEP.getCode());
        inviterSysRecord.setRemark("用户" + user.getUid() + "完善资料，系统赠送给邀请人" + inviter.getUid() + "," + setting.getInviterEP() + "EP");
        walletRecordService.add(inviterSysRecord);
        Message message = new Message();
        message.setUserId(inviter.getId());
        message.setType(MessageType.DONATE.getCode());
        message.setOrderNo(OrderNoUtil.get());
        message.setTitle("获赠EP");
        message.setDetail("用户" + user.getUid() + "完善资料，系统赠送给邀请人" + inviter.getUid() + "," + setting.getInviterEP() + "EP");
        message.setRemark("被邀请人成功完善资料赠送EP");
        messageService.add(message);
    }

    /**
     * @param user
     * @param setting
     * @throws Exception
     */
    private void sendRegisterEP(User user, SysSetting setting) throws Exception {
        //增加EP流水表记录
        epRecordService.consumeEpRecord(user, setting.getRegisterEP(), OrderNoUtil.get(), EPRecordType.BIND_PHONE_USER_SENDEP,Constant.SYSTEM_USERID,user.getId(),"");
        this.updateEpById(user.getId(), setting.getRegisterEP());
        this.updateBindEpById(user.getId(), setting.getRegisterEP());
        WalletRecord bindPhoneRecord = new WalletRecord();
        bindPhoneRecord.setOrderNo(OrderNoUtil.get());
        bindPhoneRecord.setScore(setting.getRegisterEP());
        bindPhoneRecord.setUserId(user.getId());
        bindPhoneRecord.setRecordType(RecordType.BINDPHONEEP.getCode());
        bindPhoneRecord.setRemark("uid为" + user.getUid() + "的用户成功完善资料,获赠" + setting.getRegisterEP() + "EP");
        walletRecordService.add(bindPhoneRecord);
        this.updateEpById(SYSTEM_USER_USERID, -setting.getRegisterEP());
        WalletRecord inviterSysRecord = new WalletRecord();
        inviterSysRecord.setOrderNo(OrderNoUtil.get());
        inviterSysRecord.setScore(-setting.getRegisterEP());
        inviterSysRecord.setUserId(SYSTEM_USER_USERID);
        inviterSysRecord.setRecordType(RecordType.SYSBINDPHONEEP.getCode());
        inviterSysRecord.setRemark("用户" + user.getUid() + "完善资料，系统赠送" + user.getUid() + "," + setting.getRegisterEP() + " EP");
        walletRecordService.add(inviterSysRecord);
        Message message = new Message();
        message.setUserId(user.getId());
        message.setType(MessageType.DONATE.getCode());
        message.setOrderNo(OrderNoUtil.get());
        message.setTitle("获赠EP");
        message.setDetail("uid为" + user.getUid() + "的用户成功完善资料,获赠" + setting.getRegisterEP() + "EP");
        message.setRemark("成功完善资料赠送EP");
        messageService.add(message);
    }

    @Override
    public List<User> getUpdateList(Page page) throws Exception {
        return userDao.getUpdateList(page);
    }

    @Override
    @Transactional
    public void updateGroupIsNull(String id, String groupType) throws Exception {
        this.updateGroupCount(id, groupType);
    }


    @Override
    public void checkGroupNo(String id) throws Exception {
        Page page = new Page();
        page.setPageNo(1);
        page.setPageSize(300);
        List<User> users1 = this.getLeafList(page);
        Page page2 = new Page();
        page2.setPageNo(2);
        page2.setPageSize(300);
        List<User> users2 = this.getLeafList(page2);
        Page page3 = new Page();
        page3.setPageNo(3);
        page3.setPageSize(300);
        List<User> users3 = this.getLeafList(page3);
        Page page4 = new Page();
        page4.setPageNo(4);
        page4.setPageSize(300);
        List<User> users4 = this.getLeafList(page4);
        Page page5 = new Page();
        page5.setPageNo(5);
        page5.setPageSize(300);
        List<User> users5 = this.getLeafList(page5);
        User referrer = null;
        // 获取统计层数

		/*
        new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					updateForGroup(users1, setting);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();*/
    }

    /**
     * @param users
     * @throws Exception
     */
    private void updateForGroup(List<User> users) throws Exception {
        User referrer;
        for (User user : users) {
            referrer = this.getById(user.getFirstReferrer());
            int levelNo = ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.LEVELNO),0);
            // 更新分组统计
            for (int i = 0; i < levelNo; i++) {
                if (referrer == null || referrer.getUid() < SYSTEM_USER_UID) {
                    break;
                }
                if ("A".equals(user.getGroupType())) {
                    userDao.updateGroupChildCountA(referrer.getId());
                } else if ("B".equals(user.getGroupType())) {
                    userDao.updateGroupChildCountB(referrer.getId());
                } else if ("C".equals(user.getGroupType())) {
                    userDao.updateGroupChildCountC(referrer.getId());
                } else {
                    break;
                }
                user = referrer;
                referrer = this.getById(user.getFirstReferrer());
                User updateUser = new User();
                updateUser.setUpdateTime(new Date());
                updateUser.setIsKF(StatusType.FALSE.getCode());
                userDao.update(user.getId(), updateUser);
            }

        }
    }

    @Override
    public List<User> getLeafList(Page page) throws Exception {
        List<User> leafList = userDao.getLeafList(page);
        return (List<User>) (CollectionUtils.isEmpty(leafList) ? Collections.emptyList() : leafList);
    }

    @Override
    public Integer updateAllPerformance(String id, Double performanceOne, Double performanceTwo, Double performanceThree) throws Exception {
        if(StringUtils.isEmpty(id)){
            return null;
        }
        return userDao.updateAllPerformance(id,performanceOne,performanceTwo,performanceThree);
    }

    @Override
    public void updatePerformanceOne(String userId, Double count) {
        userDao.updatePerformanceOne(userId, count);
    }

    @Override
    public void updatePerformanceTwo(String userId, Double count) {
        userDao.updatePerformanceTwo(userId, count);
    }

    @Override
    public void updatePerformanceThree(String userId, Double count) {
        userDao.updatePerformanceThree(userId, count);
    }

    /**
     * 根据商品设置的分销和赠送EP进行计算
     *
     * @param goodsWin 订单表
     * @param buyer    购买者
     */
    private Double calcAwardScore(GoodsWin goodsWin, User buyer) throws Exception {
        //获取实时性的订单数据
        goodsWin = goodsWinService.getById(goodsWin.getId());
        //抓取订单明细数据
        List<GoodsWinDetail> list = goodsWinDetailService.getListByGoodsWinId(goodsWin.getId());
        //分销及赠送EP累计
        Double totalScore = 0d;
        //单件商品则走原来的逻辑
        if (goodsWin.getOrderType().intValue() == OrderType.PURCHASE.getCode().intValue() || list == null || list.size() == 0) {
            //处理多件商品
            goodsWin.setPrice(goodsWin.getPrice() * goodsWin.getNum());
            /**GoodsIssue goodsIssue = goodsIssueService.getById(goodsWin.getIssueId());*
             * 去除期数表，modify by jay.zheng 2017/06/29
             * */
            totalScore = sendAwardScore(goodsWin.getFirstReferrerScale(), goodsWin.getSecondReferrerScale(), goodsWin.getThirdReferrerScale(), goodsWin.getBusinessSendEp(), goodsWin.getNum(), goodsWin.getPrice(), buyer, goodsWin.getOrderNo());
            return totalScore;
        }
        //多件商品需要处理不同的分销逻辑
        for (GoodsWinDetail goodsWinDetail : list) {
            totalScore += sendAwardScore(goodsWinDetail.getFirstReferrerScale(), goodsWinDetail.getSecondReferrerScale(), goodsWinDetail.getThirdReferrerScale(), goodsWinDetail.getBusinessSendEp(), goodsWinDetail.getNum(), goodsWinDetail.getPrice() * goodsWinDetail.getNum(), buyer, goodsWin.getOrderNo());
        }
        return totalScore;
    }

    /**
     * 赠送三级分销
     *
     * @param firstReferrerScale  一级分销比例
     * @param secondReferrerScale 二级分销比例
     * @param thirdReferrerScale  三级分销比例
     * @param businessSendEp      赠送的EP
     * @param num                 购买数量
     * @param price               购买单价
     * @param buyer               购买者
     */
    private Double sendAwardScore(Double firstReferrerScale, Double secondReferrerScale, Double thirdReferrerScale, Double businessSendEp, Integer num, Double price, User buyer, String orderNo) throws Exception {
        // 计算分销
        Double firstScore = 0d;
        Double secondScore = 0d;
        Double thirdScore = 0d;
        Double ep = 0d;
        ep = businessSendEp * num;
        //分销及赠送EP累计
        Double totalScore = 0d;
        if (firstReferrerScale > 0d && StringUtils.isNotBlank(buyer.getFirstReferrer())) {
            firstScore = PoundageUtil.getPoundage(price * firstReferrerScale / 100d, 1d);
            addAwardScore(firstScore, buyer.getFirstReferrer(), orderNo);
        }
        if (secondReferrerScale > 0d && StringUtils.isNotBlank(buyer.getSecondReferrer())) {
            secondScore = PoundageUtil.getPoundage(price * secondReferrerScale / 100d, 1d);
            addAwardScore(secondScore, buyer.getSecondReferrer(), orderNo);
        }
        if (thirdReferrerScale > 0d && StringUtils.isNotBlank(buyer.getThirdReferrer())) {
            thirdScore = PoundageUtil.getPoundage(price * thirdReferrerScale / 100d, 1d);
            addAwardScore(thirdScore, buyer.getThirdReferrer(), orderNo);
        }
        totalScore = totalScore + firstScore + secondScore + thirdScore + ep;
        return totalScore;
    }


    /**
     * 赠送EP
     *
     * @param goodsWin 订单信息
     */
    private void sendEp(GoodsWin goodsWin) throws Exception {
        //获取最新的订单数据
        goodsWin = goodsWinService.getById(goodsWin.getId());
        User user = this.getById(goodsWin.getUserId());
        //原逻辑一些订单数据是从期数表里获取，先保留不动
        /**
         * GoodsIssue goodsIssue = goodsIssueService.getById(goodsWin.getIssueId());
         * 去除期数表 modify by jay.zheng 2016/06/29
        * **/
        //当有多件商品结算时会产生订单明细表
        List<GoodsWinDetail> list = goodsWinDetailService.getListByGoodsWinId(goodsWin.getId());
        Double userEp = 0d;
        //原参与斗拍或单件商品直接购买
        if (goodsWin.getOrderType().intValue() == OrderType.PURCHASE.getCode().intValue() || list == null || list.size() == 0) {
            userEp = goodsWin.getBusinessSendEp() * goodsWin.getNum();
        } else {
            for (GoodsWinDetail goodsWinDetail : list) {
                userEp = userEp + goodsWinDetail.getBusinessSendEp() * goodsWinDetail.getNum();
            }
        }

        userEp = PoundageUtil.getPoundage(userEp, 1d);

        // 计算Ep,修改用户ep
        if (userEp > 0d) {
            //增加EP流水表记录
            Store store = storeService.getById(goodsWin.getStoreId());
            User userStore = this.getById(store.getUserId());
            epRecordService.consumeEpRecord(userStore, -userEp, goodsWin.getOrderNo(), EPRecordType.SENDEP,userStore.getId(),user.getId(),"");
            epRecordService.consumeEpRecord(user, userEp, goodsWin.getOrderNo(), EPRecordType.SENDEP,user.getId(),userStore.getId(),"");
            // 新增ep获赠流水记录
            BusinessEpRecord ber = new BusinessEpRecord();
            ber.setUserId(goodsWin.getUserId());
            ber.setOrderNo(goodsWin.getOrderNo());
            ber.setEp(userEp);
            ber.setRemark(RecordType.SENDEP.getMsg());
            businessEpRecordService.add(ber);

            // 新增购买ep转积分流水表
            WalletSysRecord walletSysRecord = new WalletSysRecord();
            walletSysRecord.setUserId(goodsWin.getUserId());
            walletSysRecord.setOrderNo(goodsWin.getOrderNo());
            walletSysRecord.setScore(userEp);
            walletSysRecord.setRemark(RecordType.SENDEP.getMsg());
            walletSysRecordService.add(walletSysRecord);

            // 增加EP赠送记录
            WalletRecord record = new WalletRecord();
            record.setUserId(goodsWin.getUserId());
            record.setOrderNo(goodsWin.getOrderNo());
            record.setScore(userEp);
            record.setRecordType(RecordType.SENDEP.getCode());
            record.setRemark(RecordType.SENDEP.getMsg());
            walletRecordService.add(record);

            // 新增ep消息
            addEPMessage(goodsWin, userEp);

            // 修改购买用户的ep值
            this.updateEp(goodsWin.getUserId(), userEp);

            //新增赠送与ep等额的斗斗给会员的记录
           /* WalletRecord doudouRecord = new WalletRecord();
            doudouRecord.setUserId(goodsWin.getUserId());
            doudouRecord.setOrderNo(goodsWin.getOrderNo());
            doudouRecord.setScore(goodsWin.getDoudou());
            doudouRecord.setRecordType(RecordType.PURCHASE_DOUDOU.getCode());
            doudouRecord.setRemark(RecordType.PURCHASE_DOUDOU.getMsg());
            walletRecordService.add(doudouRecord);*/

            //新增用户收到斗斗的记录
           /* WalletSign model = new WalletSign();
            model.setUserId(user.getId());
            model.setDonateUserId(user.getId());
            model.setDonateUid(user.getUid());
            model.setDoudou(goodsWin.getDoudou());
            model.setOrderNo(goodsWin.getOrderNo());
            model.setScale(0d);
            model.setType(SignType.PURCHASE_DOUDOU.getCode());
            model.setRemark(SignType.PURCHASE_DOUDOU.getMsg());
            model.setScore(goodsWin.getDoudou());
            model.setConfirmScore(goodsWin.getDoudou());
            model.setGrade(user.getGrade());
            model.setStatus(StatusType.TRUE.getCode());
            walletSignService.add(model);*/

            //增加用户的斗斗值
//            this.updateDoudou(goodsWin.getUserId(),goodsWin.getDoudou());

            /**
             * 合伙人推荐商家赠送ep业绩
             * 条件：
             *      1.合伙人加入时间大于商家加入时间
             *      2.未使用ep折扣优惠
             */
            partnerBillDetailService.createPartnerBillDetail(store.getId(),goodsWin.getOrderNo(),userEp);
        }
    }

    /**
     * 扣除用户EP，增加系统ep
     *
     * @param goodsWin 订单信息
     */
    private void addSystemEp(GoodsWin goodsWin) throws Exception {
        //获取最新的订单数据
        goodsWin = goodsWinService.getById(goodsWin.getId());

        Double userEp = goodsWin.getDiscountEP();

        // 计算Ep,修改用户ep
        if (userEp > 0d) {

            // 新增用户扣除ep流水记录
            User user = userDao.getById(goodsWin.getUserId());
            //增加EP流水表记录(直接购买ep抵扣)
            epRecordService.consumeEpRecord(user, -userEp, goodsWin.getOrderNo(), EPRecordType.SHOPPING_DISCOUNT,user.getId(),Constant.SYSTEM_USERID,"");
            //增加用户的ep消费
            this.updateConsumeEPById(user.getId(), userEp);
            ConsumeEPRecord model = new ConsumeEPRecord();
            model.setId(UUIDUtil.getUUID());
            model.setCreateTime(new Date());
            model.setUpdateTime(new Date());
            model.setConsumeEp(userEp);
            model.setStatus(1);
            model.setUserId(user.getId());
            model.setGrade(user.getGrade());
            model.setRemark("购买商品中使用了ep折扣优惠券，消费了" + userEp + "EP");
            consumeEPecordService.add(model);
            //上线会员增加销售业绩
            this.updatePerformanceCount(user.getId(), userEp);
            //新增业绩处理记录
            performanceRecordService.create(goodsWin.getOrderNo(),goodsWin.getDiscountEP(),user.getId(),goodsWin.getOrderType(),goodsWin.getRemark());
            // 增加用户扣除EP记录
            WalletRecord record = new WalletRecord();
            record.setUserId(goodsWin.getUserId());
            record.setOrderNo(goodsWin.getOrderNo());
            record.setScore(userEp);
            record.setRecordType(RecordType.USER_DISCOUNT_EP.getCode());
            record.setRemark(RecordType.USER_DISCOUNT_EP.getMsg());
            walletRecordService.add(record);

            // 新增用户使用折扣ep优惠消息
            Message message = new Message();
            message.setUserId(goodsWin.getUserId());
            message.setOrderNo(goodsWin.getOrderNo());
            message.setTitle(MessageType.EXPENSE.getMsg());
            message.setType(MessageType.EXPENSE.getCode());
            message.setDetail("您在[" + goodsWin.getStoreName() + "]商家中购买商品并使用ep折扣优惠券，消费了" + userEp + "EP！");
            message.setRemark(MessageType.EXPENSE.getMsg());
            messageService.add(message);

            // 修改系统的ep值
            this.updateEp("system", userEp);

            //增加一条赠送ep到系统流水记录
            WalletRecord sysRecord = new WalletRecord();
            sysRecord.setOrderNo(goodsWin.getOrderNo());
            sysRecord.setScore(userEp);
            sysRecord.setUserId(SYSTEM_USER_USERID);
            sysRecord.setRecordType(RecordType.USER_DISCOUNT_EP.getCode());
            sysRecord.setRemark("用户" + user.getUid() + "购买商品中使用ep折扣券,消费" + userEp + " EP");
            walletRecordService.add(sysRecord);
        }
    }

    /**
     * 系统抽取商家每笔交易的手续费(1%)
     *
     * @param orderNo 订单号
     * @param userid  用户ID
     * @param amt     商家交易获取的金额
     */
    @Override
    @Transactional
    public double tradeRate(String orderNo, String userid, double amt) throws Exception {
        logger.info("**********************************************************");
        logger.info("商家交易所得积分：" + amt);
        logger.info("订单号：" + orderNo);
        logger.info("商家ID：" + userid);
        logger.info("**********************************************************");
        if (amt <= 0) {
            return 0d;
        }
        //获取用户资料
        User user = this.getById(userid);
        //获取商家资料
        Store store = storeService.getById(user.getStoreId());
        //获取系统参数设定
        //计算系统抽取商家交易的手续费用
        double tradeRate = PoundageUtil.divide(ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.TRADERATE),0d),100,4);
        Double tradeAmt = tradeRate * amt;
        tradeAmt = PoundageUtil.getPoundage(tradeAmt, 1d);
        //抽佣小于0，忽略不计
        if (tradeAmt <= 0) {
            return 0d;
        }
        //增加系统收入
        this.updateScore("system", tradeAmt);

        // 增加EP赠送记录
        WalletRecord record = new WalletRecord();
        record.setUserId(userid);
        record.setOrderNo(orderNo);
        record.setScore(-tradeAmt);
        record.setRecordType(RecordType.SYS_TRADE_RATE.getCode());
        record.setRemark(RecordType.SYS_TRADE_RATE.getMsg());
        if (store != null) {
            record.setProvince(store.getProvince());
            record.setCity(store.getCity());
            record.setCounty(store.getCounty());
        }
        walletRecordService.add(record);
        return tradeAmt;
    }

    /**
     * 新增加入合伙人记录
     **/
    private WalletSign addWalletSign(User user, double daily) throws Exception {
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
        return walletSign;
    }

    /**
     * 加入合伙人、面对面扫码支付、商家二维码扫码支付异步处理业绩
     *
     * @param user    用户
     * @param orderNo
     */
    @Override
    @Transactional
    public void updatePermanceByThread(final User user, final String orderNo, Double cosumeEp) {
        WalletRecharge updateMode = null;
        WalletRecharge model = null;
        try {
            model = walletRechargeService.getByOrderNo(orderNo, user.getId());
            if (model == null) {
                return;
            }
            Double discountEP = 0d;
            if (model.getDiscountEP() != null && model.getDiscountEP() > 0) {
                discountEP = model.getDiscountEP();
            }
            if (cosumeEp > 0) {
                discountEP = cosumeEp;
            }
            // 修改支付订单
            updateMode = new WalletRecharge();
            if (model.getStatus() == RechargeType.TRANSFER_SUCCESS.getCode() && discountEP > 0) {
                final WalletRecharge finalUpdateMode = updateMode;
                final WalletRecharge finalModel = model;
                final Double finalDiscountEP = discountEP;
                try {
                    //新增业绩处理记录
                    PerformanceRecord performanceRecord = new PerformanceRecord();
                    performanceRecord.setOrderNo(orderNo);
                    performanceRecord.setConsumeEp(finalDiscountEP);
                    performanceRecord.setUserId(user.getId());
                    performanceRecord.setRecordType(finalModel.getSource());
                    performanceRecord.setRemark(finalModel.getRemark());
                    performanceRecordService.add(performanceRecord);
                    //开始处理业绩
                    updatePerformanceCount(user.getId(), finalDiscountEP);
                    //处理成功更新状态
                    finalUpdateMode.setStatus(RechargeType.PERFORMANCE_SUCCESS.getCode());
                    walletRechargeService.update(finalModel.getId(), finalUpdateMode);
                } catch (Exception e) {
                    try {
                        //失败回退
                        finalUpdateMode.setStatus(RechargeType.PERFORMANCE_FAIL.getCode());
                        walletRechargeService.update(finalModel.getId(), finalUpdateMode);
                    } catch (Exception e1) {
                        logger.error("60层销售业绩业务处理失败,业绩处理回滚,订单号：" + orderNo);
                    }
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            logger.error("60层销售业绩业务处理失败,业绩处理回滚,订单号：" + orderNo);
        }
    }


    /**
     * 赠送三级分销
     *
     * @param firstReferrerScale  一级分销比例
     * @param secondReferrerScale 二级分销比例
     * @param thirdReferrerScale  三级分销比例
     * @param businessSendEp      赠送的EP
     * @param num                 购买数量
     * @param price               购买单价
     * @param buyer               购买者
     *//*
    public static Double sendAwardScore1(Double firstReferrerScale, Double secondReferrerScale, Double thirdReferrerScale, Double businessSendEp, Integer num, Double price, User buyer)  {
        buyer.setFirstReferrer(8.0+"");
        buyer.setSecondReferrer(9+"");
        buyer.setThirdReferrer(10+"");
        // 计算分销
        Double firstScore = 0d;
        Double secondScore = 0d;
        Double thirdScore = 0d;
        Double ep = 0d;
        ep = businessSendEp * num;
        //分销及赠送EP累计
        Double totalScore = 0d;
        if (firstReferrerScale > 0d && StringUtils.isNotBlank(buyer.getFirstReferrer())) {
            firstScore = PoundageUtil.getPoundage(price * firstReferrerScale / 100d, 1d);
            System.out.println(firstScore);
        }
        if (secondReferrerScale > 0d && StringUtils.isNotBlank(buyer.getSecondReferrer())) {
            secondScore = PoundageUtil.getPoundage(price * secondReferrerScale / 100d, 1d);
            System.out.println(secondScore);
        }
        if (thirdReferrerScale > 0d && StringUtils.isNotBlank(buyer.getThirdReferrer())) {
            thirdScore = PoundageUtil.getPoundage(price * thirdReferrerScale / 100d, 1d);
            System.out.println(thirdScore);
        }
        totalScore = totalScore + firstScore + secondScore + thirdScore + ep;
        System.out.println(totalScore);
        return totalScore;
    }



    public static void main(String[] args) {
        sendAwardScore1(8d,9d,10d,800d,1,1680d,new User());
    }*/

    /**
     * 判断用户是否完善了资料
     * @param user
     * @return
     */
    @Override
     public  Boolean isCompleteInfo(User user){
         Boolean flag=false;
         if (user==null){
             return false;
         }
         flag = ToolUtil.isNotEmpty(user.getPhone()) && ToolUtil.isNotEmpty(user.getPassword()) && ToolUtil.isNotEmpty(user.getPayPwd())
                 && ToolUtil.isNotEmpty(user.getAreaId()) && ToolUtil.isNotEmpty(user.getProvince()) && ToolUtil.isNotEmpty(user.getCity()) && ToolUtil.isNotEmpty(user.getCounty());
         if (flag){
             return  true;
         }
         return  flag;
     }

    @Override
    @Transactional
    public void completeInfo(User user) throws Exception {
        Boolean isSend = true;
        User dataUser =this.getById(user.getId());
        if (ToolUtil.isNotEmpty(dataUser.getPhone())){
            //如果手机号不为空，说明有绑定过手机号，当作已赠送过EP，后续不赠送
            isSend=false;
        }
        this.update(user.getId(),user);
        User newUser = this.getById(user.getId());
        Boolean flag= this.isCompleteInfo(newUser);
        if (flag && isSend){
            ParamUtil util = ParamUtil.getIstance();
            /*User systemUser = this.getById(SYSTEM_USER_USERID);
            System.out.println("**********判断系统账户*************");
            boolean ex = systemUser.getExchangeEP() < (setting.getInviterEP() + setting.getRegisterEP());
            System.out.println("**********判断系统账户*************  结果 " + ex);
            if (ex) {
                logger.error(newUser.getUid() + "绑定手机号，系统ep不足");
                throw new RuntimeException("系统ep不足");
            }*/
            String orderNo = OrderNoUtil.get();
            boolean inviter = ToolUtil.parseDouble(util.get(Parameter.INVITERDOUDOU),0d) > 0;
            logger.error(inviter + "");
            System.out.println("***********************");
            if (inviter) {
                sendInviterDoudou(newUser, orderNo);
            }
            if (ToolUtil.parseDouble(util.get(Parameter.REGISTERDOUDOU),0d) > 0) {
                sendRegisterDoudou(newUser,orderNo);
            }
        }
    }

    /**
     * @param user
     * @throws Exception
     */
    private void sendRegisterDoudou(User user,String orderNo) throws Exception {
        ParamUtil util = ParamUtil.getIstance();
        Double doudou = ToolUtil.parseDouble(util.get(Parameter.REGISTERDOUDOU),0d);
        //累计用户获赠的斗斗
        this.updateBindDoudouById(user.getId(), doudou);
        this.updateDoudou(user.getId(),doudou);
        WalletRecord bindPhoneRecord = new WalletRecord();
        bindPhoneRecord.setOrderNo(orderNo);
        bindPhoneRecord.setScore(doudou);
        bindPhoneRecord.setUserId(user.getId());
        bindPhoneRecord.setRecordType(RecordType.BINDPHONEDOUDOU.getCode());
        bindPhoneRecord.setRemark("uid为" + user.getUid() + "的用户成功绑定手机号,获赠" + doudou + "斗斗");
        walletRecordService.add(bindPhoneRecord);
        Message message = new Message();
        message.setUserId(user.getId());
        message.setType(MessageType.DONATE.getCode());
        message.setOrderNo(orderNo);
        message.setTitle("获赠EP");
        message.setDetail("uid为" + user.getUid() + "的用户成功绑定手机号,获赠" + doudou + "斗斗");
        message.setRemark("成功绑定手机号赠送斗斗");
        messageService.add(message);
        //doudou获赠记录
        WalletSign model = new WalletSign();
        model.setUserId(user.getId());
        model.setDonateUserId(user.getId());
        model.setDonateUid(user.getUid());
        model.setDoudou(doudou);
        model.setOrderNo(orderNo);
        model.setScale(ToolUtil.parseDouble(util.get(Parameter.EPTODOUSCALE),0d));
        model.setType(SignType.REGISTERPHONE.getCode());
        model.setRemark(SignType.REGISTERPHONE.getMsg());
        model.setScore(doudou);
        model.setConfirmScore(doudou);
        model.setGrade(user.getGrade());
        model.setStatus(StatusType.TRUE.getCode());
        walletSignService.add(model);
    }
    /**
     * @param user
     * @throws Exception
     */
    private void sendInviterDoudou(User user,String orderNo) throws Exception {
        ParamUtil util = ParamUtil.getIstance();
        User inviter = this.getById(user.getFirstReferrer());
        if (inviter.getBindDoudou() == null) {
            inviter.setBindDoudou(0d);
        }
        double bindDoudou = ToolUtil.parseDouble(util.get(Parameter.BINDDOUDOU),0d);
        if (inviter.getGrade() < 1 || inviter.getBindDoudou() >= bindDoudou) {
            return;
        }
        Double doudou = ToolUtil.parseDouble(util.get(Parameter.INVITERDOUDOU),0d);
        this.updateBindDoudouById(inviter.getId(), doudou);
        this.updateDoudou(inviter.getId(),doudou);
        WalletRecord inviterRecord = new WalletRecord();
        inviterRecord.setOrderNo(orderNo);
        inviterRecord.setScore(doudou);
        inviterRecord.setUserId(inviter.getId());
        inviterRecord.setRecordType(RecordType.INVITERDOUDOU.getCode());
        inviterRecord.setRemark("用户" + inviter.getUid() + "邀请用户" + user.getUid() + "注册并绑定手机号，获赠" + doudou + "斗斗");
        walletRecordService.add(inviterRecord);
        Message message = new Message();
        message.setUserId(inviter.getId());
        message.setType(MessageType.DONATE.getCode());
        message.setOrderNo(OrderNoUtil.get());
        message.setTitle("获赠斗斗");
        message.setDetail("用户" + user.getUid() + "绑定手机号，系统赠送给邀请人" + inviter.getUid() + "," + doudou + "斗斗");
        message.setRemark("被邀请人成功绑定手机号赠送斗斗");
        messageService.add(message);
        //doudou获赠记录
        WalletSign model = new WalletSign();
        model.setUserId(inviter.getId());
        model.setDonateUserId(inviter.getId());
        model.setDonateUid(inviter.getUid());
        model.setDoudou(doudou);
        model.setOrderNo(orderNo);
        model.setScale(ToolUtil.parseDouble(util.get(Parameter.EPTODOUSCALE),0d));
        model.setType(SignType.BINDPHONE.getCode());
        model.setRemark(SignType.BINDPHONE.getMsg());
        model.setScore(doudou);
        model.setConfirmScore(doudou);
        model.setGrade(inviter.getGrade());
        model.setStatus(StatusType.TRUE.getCode());
        walletSignService.add(model);
    }
}
