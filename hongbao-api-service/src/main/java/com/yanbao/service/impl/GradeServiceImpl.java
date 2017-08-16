package com.yanbao.service.impl;

import com.yanbao.constant.*;
import com.yanbao.dao.GradeDao;
import com.mall.model.*;
import com.yanbao.service.*;
import com.yanbao.util.OrderNoUtil;
import com.yanbao.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author zcj
 * @date 2017年03月02日
 */

@Service
public class GradeServiceImpl implements GradeService{
    private static final Logger logger = LoggerFactory.getLogger(GradeServiceImpl.class);
    private static final Integer SYSUSERID = 200000;
    @Autowired
    private GradeDao gradeDao;
    @Autowired
    private WalletSignService walletSignService;
    @Autowired
    private WalletRecordService walletRecordService;
    @Autowired
    private ConsumeEPRecordService consumeEPRecordService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private WalletSubsidySignService subsidySignService;
    @Autowired
    private EPRecordService epRecordService;

    @Override
    public Grade getGradeDetil(Integer grade) throws Exception{
        return gradeDao.getGradeDetil(grade);
    }

    @Override
    @Transactional
    public Double memberSignIn(User user) throws Exception {
        String msg = "";
        /**获取会员最新等级*/
        Integer newGrade = getNewGrade(user);
        /**领取积分阶段会员等级只升不降*/
        if (newGrade > user.getGrade() && user.getRemainSign() > 0) {
            this.updatePartnerInfo(user.getId());
            user = userService.getById(user.getId());
        }
        /**最新会员等级的基本信息*/
        Grade grade = gradeDao.getGradeDetil(user.getGrade());
        Integer daily = grade.getDaily();
        //今天可领取领取积分
        user.setScore(user.getScore() + daily);
        //扣除领取次数
        user.setRemainSign(user.getRemainSign() - 1);
        //更新签到时间
        user.setSignTime(new Date());
        userService.updateBySignIn(user.getId(), user);
        // 增加签到消息
        int count = grade.getTotalSignNo() - user.getRemainSign();
        if(count ==1){
            msg = "恭喜您升级为 " + GradeType.fromCode(user.getGrade()).getMsg() + "等级";
        }
        //签到轮次
        int signCount = walletSignService.getSignCount(user.getId());
          /*记录签到流水*/
        WalletSign walletSign=addWalletSign(user,grade,count,signCount, SignType.PARTENER_SIGN,SignType.PARTENER_SIGN.getMsg());
        // 增加签到积分流水
        WalletRecord record = new WalletRecord();
        record.setUserId(user.getId());
        record.setOrderNo(walletSign.getOrderNo());
        record.setScore(daily.doubleValue());
        record.setRecordType(RecordType.SIGNEP.getCode());
        record.setRemark(RecordType.SIGNEP.getMsg());
        walletRecordService.add(record);
        //最后一次签到重新计算会员等级
        if (count == 30) {
            //重新计算会员等级
            Integer reloadGrade = getNewGrade(user);
            Grade gradeinfo = gradeDao.getGradeDetil(reloadGrade);
            User model = new User();
            model.setRemainSign(gradeinfo.getTotalSignNo());
            model.setGrade(newGrade);
            userService.update(user.getId(), model);
            /*记录会员升级*/
            this.addWalletSign(user, gradeinfo, 0, 0, SignType.UPGRADE, "会员30次签到次数领完，重新开始计算业绩!");
            //清算业绩,等级大于VIP才清除业绩
            if (reloadGrade.intValue() > GradeType.grade2.getCode().intValue()) {
                clearAllPerformance(user, reloadGrade);
            }
            msg = "本轮是【" + GradeType.fromCode(user.getGrade()).getMsg() + "】的最后一轮签到，签到清算后的等级为" + GradeType.fromCode(reloadGrade).getMsg() + ",请知悉!";
        }
        /**
         *
         * 系统账户扣除会员领取的EP积分
         * */
        User cond = new User();
        cond.setUid(SYSUSERID);
        User sysUser = userService.getByCondition(cond);
        userService.updateEp(sysUser.getId(), -walletSign.getConfirmScore());
        //扣除领取金额等比例的斗斗数量
        userService.updateDoudou(user.getId(), -walletSign.getConfirmScore());
        //记录系统账户扣除EP的记录
        epRecordService.consumeEpRecord(sysUser, -walletSign.getConfirmScore(), walletSign.getOrderNo(), EPRecordType.PARTNER_SIGNIN,Constant.SYSTEM_USERID,null,"合伙人签到，扣除200000账号对应斗斗数量EP");
        String content = "签到成功！第" + count + "次签到，领取" + daily + "金额！" + msg;
        addMessage(user.getId(), "签到消息", content);
        return daily.doubleValue();
    }

    private WalletSign addWalletSign(User user, Grade grade, int count, int signCount, SignType signType, String remark) throws Exception {
           /*记录签到流水*/
        WalletSign walletSign = new WalletSign();
        walletSign.setUserId(user.getId());
        walletSign.setDonateUserId(user.getId());
        walletSign.setDonateUid(user.getUid());
        walletSign.setScore(grade.getDaily().doubleValue());
        walletSign.setConfirmScore(grade.getDaily().doubleValue());
        walletSign.setStatus(StatusType.TRUE.getCode());
        walletSign.setSignNo(count);
        walletSign.setGrade(grade.getGrade());
        walletSign.setSignCount(signCount);
        walletSign.setType(signType.getCode());
        walletSign.setRemark(remark);
        walletSign.setDoudou(walletSign.getConfirmScore());
        //续费后签到轮次+1
        if (count == 1) {
            walletSign.setSignCount(signCount + 1);
        }
        walletSignService.add(walletSign);
        return walletSign;
    }

    @Override
    @Transactional
    public boolean renewGrade(String userid) throws Exception {
        //保证用户信息是最新的
        User user = userService.getById(userid);
        if(user == null){
            logger.error("找不到用户："+userid);
            return false;
        }
        /**剩余签到领取积分次数等于0，是否自动续费*/
        if(user.getRemainSign()>0){
            return false;
        }
        Grade calcGrade = gradeDao.getGradeDetil(user.getGrade());
        if(user.getConsumeEP() < calcGrade.getEP()){
            // 增加续费失败的系统消息
            String content = "当前等级为："+ GradeType.fromCode(user.getGrade()).getMsg()+";续费所需消费EP:"
                    +calcGrade.getEP()+";当前用户持有的消费EP:"+user.getConsumeEP()+";续费失败！请到兑换专区进行消费以获得消费EP";
            logger.debug(content);
            addMessage(user.getId(),"续费消息",content);
            return false;
        }
        //重新计算会员等级
        Integer upgrade = getNewGrade(user);
        //重新计算签到次数
        Integer totalSignNo = calcGrade.getTotalSignNo();
        if(totalSignNo == null || totalSignNo ==0){
            totalSignNo = 30;
        }
        user.setRemainSign(totalSignNo);
        //更新会员等级
        user.setGrade(upgrade);
        //续签成功后扣除会员的消费EP值
        user.setConsumeEP(user.getConsumeEP() - calcGrade.getEP());
        userService.updateBySignIn(user.getId(),user);
        //系统账户增加会员续签的EP积分
        User cond = new User();
        cond.setUid(SYSUSERID);
        User sysUser = userService.getByCondition(cond);
        userService.updateEp(sysUser.getId(),calcGrade.getEP());
        /*记录会员消费EP流水*/
        ConsumeEPRecord epRecord = new ConsumeEPRecord();
        epRecord.setUserId(user.getId());
        epRecord.setConsumeEp(-calcGrade.getEP());
        epRecord.setStatus(StatusType.TRUE.getCode());
        epRecord.setType(ConsumeEPType.UPGRADE.getCode());
        epRecord.setGrade(calcGrade.getGrade());
        epRecord.setRemark(ConsumeEPType.UPGRADE.getMsg());
        consumeEPRecordService.add(epRecord);
        // 增加积分流水
        /*WalletRecord record = new WalletRecord();
        record.setUserId(user.getId());
        record.setOrderNo(epRecord.getOrderNo());
        record.setScore(calcGrade.getEP());
        record.setRecordType(RecordType.CONSUMEEP.getCode());
        record.setRemark(RecordType.CONSUMEEP.getMsg());
        walletRecordService.add(record);*/
        // 增加续费成功的系统消息
        String content = "会员续费成功，续费扣除用户消费EP:"+calcGrade.getEP()+",会员最新等级："+GradeType.fromCode(user.getGrade()).getMsg();
        logger.debug(content);
        addMessage(user.getId(),"续费消息",content);
        //清算业绩
        clearAllPerformance(user,upgrade);
        return true;
    }

    /**补贴签到积分*/
    @Transactional
    @Override
    public WalletSubsidySign getSubsidyAmt(String userid) throws Exception{
        WalletSubsidySign model = subsidySignService.getByUserId(userid,StatusType.FALSE.getCode());
        if(model == null){
            logger.info("该用户没有需要补贴的信息");
            return null;
        }
        if(model.getStatus().intValue() == StatusType.TRUE.getCode().intValue()){
            logger.info("用户："+userid+" 已全部补贴完毕!!");
            return null;
        }
        //补贴次数
        WalletSubsidySign newSubsidySign = new WalletSubsidySign();
        newSubsidySign.setStatus(StatusType.TRUE.getCode());
        newSubsidySign.setUpdateTime(new Date());
        newSubsidySign.setSignTime(new Date());
        subsidySignService.update(newSubsidySign,model.getId());
        return model;
    }

    /**会员续费计算所需的签到补贴信息*/
    @Transactional
    private void reloadSubsidyInfo(Integer grade, String userid, int signCount) throws Exception {
        User user = userService.getById(userid);
        //所处等级签到次数
        Integer signSecond = walletSignService.getSubsidyCount(userid,grade.toString());
        Grade gradest = gradeDao.getGradeDetil(grade);
        //剩余签到次数
        Integer subSignCount = gradest.getTotalSignNo()-signSecond;
        WalletSubsidySign model = new WalletSubsidySign();
        model.setSubsidyNo(subSignCount);
        model.setGrade(grade);
        model.setUserId(userid);
        model.setDonateUserId(userid);
        model.setDonateUid(user.getUid());
        model.setStatus(StatusType.FALSE.getCode());
        model.setScore(gradest.getDaily().doubleValue());
        model.setConfirmScore(gradest.getDaily()*subSignCount.doubleValue());
        model.setCreateTime(new Date());
        model.setId(UUIDUtil.getUUID());
        model.setOrderNo(OrderNoUtil.get());
        model.setSignCount(signCount);
        subsidySignService.add(model);
    }

    /**获取最新等级*/
    @Override
    public Integer getNewGrade(User user) throws Exception{
        //根据用户下线三个销售部做排序(由小到大)
        Double[] arrGrade = {user.getPerformanceOne(),user.getPerformanceTwo(),user.getPerformanceThree()};
        Arrays.sort(arrGrade);
        return  gradeDao.getMemberGrade(arrGrade[0],arrGrade[1]);
    }
    private void addMessage(String userId,String title,String content) throws Exception {
        Message message = new Message();
        message.setUserId(userId);
        message.setTitle(title);
        message.setType(MessageType.SYSTEM.getCode());
        message.setDetail(content);
        message.setRemark(title);
        messageService.add(message);
    }
    //清算销售业绩
    @Transactional
    @Override
    public void clearAllPerformance(User user, Integer newGrade) throws Exception {
        //保证用户数据是最新的
        user = userService.getById(user.getId());
        if(null == user){
            logger.error("用户不存在："+user.getId());
            return;
        }
        //第一次加入合伙人，清空用户所有的业绩
        if(newGrade.intValue() == GradeType.grade2.getCode().intValue()){
            userService.updateAllPerformance(user.getId(),0d,0d,0d);
            return;
        }
        //扣除升级所需业绩
        Grade dissGrade = this.getGradeDetil(newGrade);
        //根据用户下线三个销售部做排序(由小到大)
        Double countA = user.getPerformanceOne();
        Double countB = user.getPerformanceTwo();
        Double countC = user.getPerformanceThree();
        if(countA <= countB && countA <= countC){
            userService.updateAllPerformance(user.getId(),(countA-dissGrade.getPerformanceThree()),(countB-dissGrade.getPerformanceTwo()),(countC-dissGrade.getPerformanceTwo()));
        }else if(countB <= countA && countB <= countC){
            userService.updateAllPerformance(user.getId(),(countA-dissGrade.getPerformanceOne()),(countB-dissGrade.getPerformanceThree()),(countC-dissGrade.getPerformanceTwo()));
        }else {
            userService.updateAllPerformance(user.getId(),(countA-dissGrade.getPerformanceOne()),(countB-dissGrade.getPerformanceTwo()),(countC-dissGrade.getPerformanceThree()));
        }
    }

    //更新合伙人等级
    @Override
    @Transactional
    public void updatePartnerInfo(String userId) throws Exception {
        //获取最新用户数据
        User user = userService.getById(userId);
        if (user.getGrade().intValue() < GradeType.grade2.getCode()) {
            logger.info("用户：" + user.getUserName() + "(" + user.getUid() + ")未加入合伙人！！！");
            return;
        }
        // 获取合伙人能达到的最新会员等级
        int newGrade = this.getNewGrade(user);
        if (newGrade > user.getGrade()) {
            //获取下一等级的参照信息
            int gradeOrdinal = GradeType.fromCode(user.getGrade()).ordinal() + 1;
            int updGrade = GradeType.fromOrdinal(gradeOrdinal).getCode();
            //从VIP越级到初级合伙人以上，需忽略初级合伙人这一条件设置，从一星合伙人开始清理
            if(user.getGrade().intValue() == GradeType.grade2.getCode().intValue() && newGrade > GradeType.grade3.getCode()){
                updGrade = GradeType.grade4.getCode();
            }
            Grade grade = this.getGradeDetil(updGrade);
            String log = "用户：" + user.getUserName() + "(" + user.getUid() + ")等级为【" + GradeType.fromCode(user.getGrade()).getMsg() + "】满足下等级条件,现在到下一等级【" + GradeType.fromCode(grade.getGrade()).getMsg() + "】!!!!";
            logger.debug(log);
            Integer totalSignNo = grade.getTotalSignNo();
            if (totalSignNo == null || totalSignNo == 0) {
                totalSignNo = 30;
            }
            user.setGrade(updGrade);
            user.setRemainSign(totalSignNo);
            userService.updateBySignIn(user.getId(), user);
            //清算业绩
            this.clearAllPerformance(user, updGrade);
            /*记录会员升级*/
            this.addWalletSign(user, grade, 0, 0, SignType.UPGRADE, log);
            //递归回调，继续计算会员等级
            updatePartnerInfo(user.getId());
        } else {
            logger.debug("用户：" + user.getUserName() + "(" + user.getUid() + ")等级为【" + GradeType.fromCode(user.getGrade()).getMsg() + "】必须满足下等级条件才可升级!!!!");
            //结束递归
            return;
        }
    }

}
