package com.yanbao.controller;

import com.mall.model.Grade;
import com.mall.model.Parameter;
import com.mall.model.User;
import com.mall.model.WalletSign;
import com.yanbao.constant.SignType;
import com.yanbao.constant.StatusType;
import com.yanbao.constant.SwitchType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.service.GradeService;
import com.yanbao.service.UserService;
import com.yanbao.service.WalletExchangeService;
import com.yanbao.service.WalletSignService;
import com.yanbao.util.*;
import com.yanbao.vo.EPexchangeVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/* 
 * 文件名：WalletSignController.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：zzwei 
 * 创建时间：2017年3月3日
 * 版本号：v1.0
*/
@RestController
@RequestMapping("/sign")
public class WalletSignController {
    Logger logger = LoggerFactory.getLogger(WalletSignController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private WalletSignService walletSignService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private WalletExchangeService walletExchangeService;

    /*
     * 查询签到流水
     */
    @ResponseBody
    @RequestMapping(value = "/signlist", method = RequestMethod.GET)
    public JsonResult getSignList(HttpServletRequest request, Page page) throws Exception {

        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(1, "用户登录失效");
        }
        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(2, "无法找到用户信息");
        }
        if (user.getStatus() == StatusType.FALSE.getCode()) {
            return new JsonResult(3, "您的帐号已被禁用");
        }
        if (page.getPageNo() == 0) {
            page.setPageNo(0);
        }
        if (page.getPageSize() == 0) {
            page.setPageSize(10);
        }
        Map<String, Object> result = new HashMap<>();
        List<WalletSign> list = walletSignService.getList(token.getId(), page);
        List<Map<String, Object>> maps = new ArrayList<>();
        result.put("pageNo", page.getPageNo());
        result.put("pageSize", page.getPageSize());
        /**
         Integer totalSignNo=null;
         Grade grade = gradeService.getGradeDetil(user.getGrade());
         if (grade!=null) {
         totalSignNo = grade.getTotalSignNo();
         }
         if (grade!=null) {
         totalSignNo = 30;
         }
         Integer signNo=totalSignNo-user.getRemainSign();
         */
        if (CollectionUtils.isNotEmpty(list)) {
            for (WalletSign walletSign : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("signDate", walletSign.getCreateTime().getTime());
                map.put("signNo", walletSign.getSignNo());
                map.put("signScore", walletSign.getConfirmScore());
                maps.add(map);
            }
        }
        Double award = 0.0d;
        Grade grade = gradeService.getGradeDetil(user.getGrade());
        if (grade != null && grade.getTotalSignNo() != null && user.getRemainSign() >= 0) {
            WalletSign model = new WalletSign();
            model.setDonateUserId(user.getId());
            award = walletSignService.signTotal(model, grade.getTotalSignNo() - user.getRemainSign());
        }
        result.put("rows", maps);
        result.put("award", award);
        return new JsonResult(result);
    }

    @ResponseBody
    @RequestMapping(value = "/doudouSignIn", method = RequestMethod.GET)
    public JsonResult doudouSign(HttpServletRequest request) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(1, "用户登录失效");
        }

        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(2, "无法找到用户信息");
        }
        if (user.getStatus() == StatusType.FALSE.getCode()) {
            return new JsonResult(3, "您的帐号已被禁用");
        }
        if (user.getDouSignTime() != null) {
            Integer d1 = DateTimeUtil.getYearMonthDay(Calendar.getInstance());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(user.getDouSignTime());
            Integer d2 = DateTimeUtil.getYearMonthDay(calendar);
            if (d1.intValue() == d2.intValue()) {
                logger.debug("用户：" + user.getUserName() + "(" + user.getUid() + ")今天已签到过，不需重复签到");
                return new JsonResult(5, "已签到过，请勿重复签到");
            }
        }
        ParamUtil util = ParamUtil.getIstance();
        Double minDouNum = ToolUtil.parseDouble(util.get(Parameter.MINSIGNDOUNUM),0d);
        if (user.getDoudou() < minDouNum) {
            logger.debug("用户：" + user.getUserName() + "(" + user.getUid() + ")的斗斗数量小于" + minDouNum + "，未达到领取金额的数量");
            return new JsonResult(4, "您的斗斗数量不足" + minDouNum + ",请补充斗斗。");
        }
        //普通会员每天签到可领取的金额=持有的斗斗数量*普通会员兑换金额的比例
        Double doudou = user.getDoudou() * PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.COMMONRELEASESCALE), 0d),100,4);
        //加入过合伙人的
        if (user.getGrade() >= 1) {
            doudou = user.getDoudou() * PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.VIPRELEASESCALE), 0d),100,4);
        }
        doudou = PoundageUtil.getPoundage(doudou, 1d);
        //可领取的金额小于0.01就不进行签到
        if (doudou < 0.01) {
            logger.debug("用户：" + user.getUserName() + "(" + user.getUid() + ")可领取的金额小于0.01，请补充斗斗。");
            return new JsonResult(5, "您的斗斗数量不足" + minDouNum + ",请补充斗斗。");
        }
        // 会员签到
        Double signDouNum = walletSignService.doudouSignIn(user.getId());
        Map<String, String> map = new HashedMap();
        map.put("signDouNum", signDouNum + "");
        return new JsonResult(map);
    }


    /**
     * EP兑换斗斗
     */
    @ResponseBody
    @RequestMapping(value = "/epexchange", method = RequestMethod.POST)
    public JsonResult exchange(HttpServletRequest request, @RequestBody EPexchangeVo vo) throws Exception {
        // 检查后台是否开启兑换开关
        ParamUtil util = ParamUtil.getIstance();
        if (util == null) {
            return new JsonResult(-1, "兑换斗斗正在升级维护");
        }
        if (SwitchType.OFF.getCode().equals(ToolUtil.parseInt(util.get(Parameter.EPSWITCH),0))) {
            return new JsonResult(-1, "EP兑换斗斗功能暂未开启");
        }
        Token token = TokenUtil.getSessionUser(request);
        double minEPConvertNum = ToolUtil.parseDouble(util.get(Parameter.MINEPCONVERTNUM),0d);
        double maxEPConvertNum = ToolUtil.parseDouble(util.get(Parameter.MAXEPCONVERTNUM),0d);
        if (vo.getEp() == null || vo.getEp() < minEPConvertNum|| vo.getEp() > maxEPConvertNum) {
            return new JsonResult(1, "单笔兑换EP必须在[" + minEPConvertNum + "," + maxEPConvertNum + "]之间");
        }
        if (ToolUtil.isEmpty(vo.getPayPwd())) {
            return new JsonResult(2, "支付密码不能为空");
        }

        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException("查询不到用户信息");
        }
        if (ToolUtil.isEmpty(user.getPayPwd())) {
            return new JsonResult(3, "请先设置支付密码");
        }
        if (!user.getPayPwd().equals(Md5Util.MD5Encode(vo.getPayPwd(), user.getSalt()))) {
            return new JsonResult(4, "支付密码不正确");
        }
        if (user.getExchangeEP() == null || user.getExchangeEP() < vo.getEp()) {
            return new JsonResult(5, "您的EP不足,无法完成兑换");
        }
        if (vo.getEp()%100!=0) {
            return new JsonResult(6, "EP兑换数量只能是100的整数倍");
        }
        //处理兑换业务
        walletExchangeService.epToDouDouExchangeHandler(user, vo.getEp());
        // 操作成功返回用户当前EP,余额，斗斗
        Map<String, Object> result = new HashMap<String, Object>();
        user = userService.getById(token.getId());
        result.put("score", FormatUtils.formatDouble(user.getScore()));
        result.put("exchangeEP", FormatUtils.formatDouble(user.getExchangeEP()));
        result.put("doudou", FormatUtils.formatDouble(user.getDoudou()));
        return new JsonResult(result);
    }

    /**
     * 查询万三万五斗斗签到记录
     * @param request
     * @param page
     * @param type
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/commonsignlist", method = RequestMethod.GET)
    public JsonResult partnerSignList(HttpServletRequest request, Page page, String type) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(1, "用户登录失效");
        }
        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(2, "无法找到用户信息");
        }
        if (user.getStatus() == StatusType.FALSE.getCode()) {
            return new JsonResult(3, "您的帐号已被禁用");
        }
        if (page.getPageNo() == 0) {
            page.setPageNo(0);
        }
        if (page.getPageSize() == 0) {
            page.setPageSize(10);
        }
        Map<String, Object> result = new HashMap<>();
        Integer count = walletSignService.countCommonSignListSize(user.getId());
        List<WalletSign> list = walletSignService.getCommonSignList(token.getId(), page);
        List<Map<String, Object>> maps = new ArrayList<>();
        result.put("pageNo", page.getPageNo());
        result.put("pageSize", page.getPageSize());
        if (CollectionUtils.isNotEmpty(list)) {
            for (WalletSign walletSign : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("tradeDate", walletSign.getCreateTime().getTime());
                map.put("tradeType", walletSign.getSignNo());
                map.put("tradeTypeDesc", SignType.fromCode(walletSign.getType()).getMsg());
                map.put("tradeAmount", walletSign.getConfirmScore());
                maps.add(map);
            }
        }
        Double award = 0.0d;
        Grade grade = gradeService.getGradeDetil(user.getGrade());
        if (grade != null && grade.getTotalSignNo() != null && user.getRemainSign() >= 0) {
            WalletSign model = new WalletSign();
            model.setDonateUserId(user.getId());
            award = walletSignService.signTotal(model, grade.getTotalSignNo() - user.getRemainSign());
        }
        result.put("rows", maps);
        result.put("doudou", user.getDoudou());
        result.put("totalSize", count);
        return new JsonResult(result);
    }
    /**
     *查询所有斗斗签到记录
     */
    @ResponseBody
    @RequestMapping(value = "/doudousignlist", method = RequestMethod.GET)
    public JsonResult doudouSignList(HttpServletRequest request, Page page, String type) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(1, "用户登录失效");
        }
        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(2, "无法找到用户信息");
        }
        if (user.getStatus() == StatusType.FALSE.getCode()) {
            return new JsonResult(3, "您的帐号已被禁用");
        }
        if (page.getPageNo() == 0) {
            page.setPageNo(0);
        }
        if (page.getPageSize() == 0) {
            page.setPageSize(10);
        }
        Map<String, Object> result = new HashMap<>();
        Integer count = walletSignService.countMyDoudouListSize(user.getId());
        List<WalletSign> list = walletSignService.getMyDoudouList(token.getId(), page);
        List<Map<String, Object>> maps = new ArrayList<>();
        result.put("pageNo", page.getPageNo());
        result.put("pageSize", page.getPageSize());
        if (CollectionUtils.isNotEmpty(list)) {
            for (WalletSign walletSign : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("tradeDate", walletSign.getCreateTime().getTime());
                map.put("tradeType", walletSign.getSignNo());
                map.put("tradeTypeDesc", SignType.fromCode(walletSign.getType()).getMsg());
                map.put("tradeAmount", walletSign.getConfirmScore());
                maps.add(map);
            }
        }
        Double award = 0.0d;
        Grade grade = gradeService.getGradeDetil(user.getGrade());
        if (grade != null && grade.getTotalSignNo() != null && user.getRemainSign() >= 0) {
            WalletSign model = new WalletSign();
            model.setDonateUserId(user.getId());
            award = walletSignService.signTotal(model, grade.getTotalSignNo() - user.getRemainSign());
        }
        //判断是否已签到过
        boolean isSignIn = false;
        if (user.getDouSignTime() != null) {
            Integer d1 = DateTimeUtil.getYearMonthDay(Calendar.getInstance());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(user.getDouSignTime());
            Integer d2 = DateTimeUtil.getYearMonthDay(calendar);
            isSignIn = d1.intValue() == d2.intValue();
        }
        result.put("isSignIn", isSignIn);
        result.put("rows", maps);
        result.put("doudou", user.getDoudou());
        result.put("totalSize", count);
        return new JsonResult(result);
    }


    /**
     * 查询用户斗斗和合伙人签到的信息
     */
    @ResponseBody
    @RequestMapping(value = "/getSignInInfo", method = RequestMethod.GET)
    public JsonResult signInInfo(HttpServletRequest request) throws Exception {
        boolean doudouSignIn = true;//斗斗是否可以签到
        boolean partnerSignIn = true;//合伙人是否可以签到

        String doudouErrorMsg = "";//斗斗签到错误信息
        String partnerErrorMsg = "";//合伙人签到错误信息

        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            logger.error("用户登录失效");
            doudouErrorMsg = partnerErrorMsg = "用户登录失效";
            doudouSignIn = false;
            partnerSignIn = false;
        }

        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error("无法找到用户信息");
            doudouErrorMsg = partnerErrorMsg = "无法找到用户信息";
            doudouSignIn = false;
            partnerSignIn = false;
        }

        if (user.getStatus() == StatusType.FALSE.getCode()) {
            logger.error("您的帐号已被禁用");
            doudouErrorMsg = partnerErrorMsg = "您的帐号已被禁用";
            doudouSignIn = false;
            partnerSignIn = false;
        }


        /**合伙人签到的信息**/
        // 已经加入合伙人的但等级未达到领取积分的，需要先计算会员等级
        gradeService.updatePartnerInfo(token.getId());
        user = userService.getById(token.getId());

        if ((user.getGrade() == null || user.getGrade() <= 1) && partnerSignIn) {
            logger.debug("用户：" + user.getUserName() + "(" + user.getUid() + ")未达到领取积分的级别");
            partnerErrorMsg = "用户：" + user.getUserName() + "(" + user.getUid() + ")未达到领取积分的级别";
            partnerSignIn = false;
        }

        //今天是否已签到过
        if (user.getSignTime() != null && partnerSignIn) {
            Integer d1 = DateTimeUtil.getYearMonthDay(Calendar.getInstance());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(user.getSignTime());
            Integer d2 = DateTimeUtil.getYearMonthDay(calendar);
            if (d1.intValue() == d2.intValue()) {
                logger.debug("用户：" + user.getUserName() + "(" + user.getUid() + ")今天已签到过，不需重复签到");
                partnerErrorMsg = "用户：" + user.getUserName() + "(" + user.getUid() + ")今天已签到过，不需重复签到";
                partnerSignIn = false;
            }
        }

        if (user.getRemainSign() <= 0 && partnerSignIn) {
            logger.debug("用户：" + user.getUserName() + "(" + user.getUid() + ")剩余签到次数小于等于0，不需要签到。");
            partnerErrorMsg = "用户：" + user.getUserName() + "(" + user.getUid() + ")剩余签到次数小于等于0，不需要签到。";
            partnerSignIn = false;
        }

        //获取会员最新等级
        int newGrade = user.getGrade();
        //签到领取的日薪需要扣除对应的用户持有的斗斗数量
        Grade grade = gradeService.getGradeDetil(newGrade);
        if (user.getDoudou() < grade.getDaily() && partnerSignIn) {
            logger.debug("用户：" + user.getUserName() + "(" + user.getUid() + ")持有斗斗数量（" + user.getDoudou() + "）不足,今天可领取金额为：" + grade.getDaily());
            partnerErrorMsg = "用户：" + user.getUserName() + "(" + user.getUid() + ")持有斗斗数量（" + user.getDoudou() + "）不足,今天可领取金额为：" + grade.getDaily();
            partnerSignIn = false;
        }

        /**斗斗签到的信息**/
        if (user.getDouSignTime() != null && doudouSignIn) {
            Integer d1 = DateTimeUtil.getYearMonthDay(Calendar.getInstance());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(user.getDouSignTime());
            Integer d2 = DateTimeUtil.getYearMonthDay(calendar);
            if (d1.intValue() == d2.intValue()) {
                logger.debug("用户：" + user.getUserName() + "(" + user.getUid() + ")今天已签到过，不需重复签到");
                doudouErrorMsg = "用户：" + user.getUserName() + "(" + user.getUid() + ")今天已签到过，不需重复签到";
                doudouSignIn = false;
            }
        }

        ParamUtil util = ParamUtil.getIstance();
        Double minDouNum = ToolUtil.parseDouble(util.get(Parameter.MINSIGNDOUNUM),0d);
        double userDoudou = PoundageUtil.getPoundage(user.getDoudou() - grade.getDaily(), 1d);
        if (userDoudou < minDouNum && doudouSignIn) {
            logger.debug("用户：" + user.getUserName() + "(" + user.getUid() + ")的斗斗数量小于" + minDouNum + "，未达到领取金额的数量");
            doudouErrorMsg = "用户：" + user.getUserName() + "(" + user.getUid() + ")的斗斗数量小于" + minDouNum + "，未达到领取金额的数量";
            doudouSignIn = false;
        }

        if (doudouSignIn ) {
            //普通会员每天签到可领取的金额=持有的斗斗数量*普通会员兑换金额的比例
            Double doudou = userDoudou * PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.COMMONRELEASESCALE),0d),100,4);
            //加入过合伙人的
            if (user.getGrade() >= 1) {
                doudou = userDoudou * PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.VIPRELEASESCALE),0d),100,4);
            }
            doudou = PoundageUtil.getPoundage(doudou, 1d);
            if (doudou < 0.01) {
                logger.debug("用户：" + user.getUserName() + "(" + user.getUid() + ")可领取的金额小于0.01，请补充斗斗。");
                doudouErrorMsg = "用户：" + user.getUserName() + "(" + user.getUid() + ")可领取的金额小于0.01，请补充斗斗。";
                doudouSignIn = false;
            }
        }

        Map<Object, Object> map = new HashMap<>();
        map.put("isSignInByDoudou", doudouSignIn);//斗斗是否可以签到
        map.put("doudouErrorMsg", doudouErrorMsg);//斗斗签到错误信息
        map.put("isSignInByPartner", partnerSignIn);//合伙人是否可以签到
        map.put("partnerErrorMsg", partnerErrorMsg);//合伙人签到错误信息
        return new JsonResult(map);
    }


}


