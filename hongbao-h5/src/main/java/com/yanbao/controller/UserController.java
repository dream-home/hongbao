package com.yanbao.controller;

import com.alibaba.fastjson.JSON;
import com.mall.model.*;
import com.yanbao.constant.RedisKey;
import com.yanbao.constant.StatusType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.redis.Sets;
import com.yanbao.redis.Strings;
import com.yanbao.service.*;
import com.yanbao.util.*;
import com.yanbao.vo.ShareVo;
import com.yanbao.vo.UserVo;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final Integer SYSTEM_USER_UID = 200000;
    private static final String PAGE_SHARE = "http://doupaimall.com/h5";

    public static List<String> group = new ArrayList<>();

    static {
        group.add("A");
        group.add("B");
        group.add("C");
    }

    @Autowired
    private UserService userService;
    @Autowired
    private BankService bankService;
    @Autowired
    private UserBankcardService userBankcardService;
    @Autowired
    private UserAddressService userAddressService;
    @Value("${wechath5_callback_domain}")
    private String wechatCallbackDomain;
    @Value("${file_root_path}")
    private String FILE_ROOT_PATH;
    @Value("${h5page}")
    private String h5page;
    @Value("${user_logo}")
    private String USER_LOGO;
    @Value("${error_page}")
    private String ERROR_PAGE;
    @Autowired
    private StoreService storeService;

    /**
     * 微信登录
     */
    @ResponseBody
    @RequestMapping(value = "/login/weixin", method = RequestMethod.POST)
    public JsonResult loginByWeixin(HttpServletRequest request, @RequestBody User loginUser) throws Exception {
        if (StringUtils.isBlank(loginUser.getWeixin())) {
            return new JsonResult(-1, "openid不能为空");
        }
        User condition = new User();
        condition.setWeixin(loginUser.getWeixin());
        User user = userService.getByCondition(condition);
        if (null == user) {
            String nickName = loginUser.getNickName();
            if (StringUtils.isNotBlank(nickName))
                nickName = nickName.replaceAll("[^\u0000-\uFFFF]", "?"); // 过滤非UTF-8字符集,用"?"代替，如Emoji表情
            loginUser.setNickName(nickName);
            userService.add(loginUser);
            user = userService.getByCondition(condition);
        }
        if (StringUtils.isBlank(user.getFirstReferrer())) {
            if (loginUser.getInviteCode() == null) {
                if (StatusType.TRUE.getCode().equals(ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.INVITESWICH), 0))) {
                    return new JsonResult(10000, "请输入邀请码");
                } else { // 关闭邀请开关，默认系统邀请
                    loginUser.setInviteCode(SYSTEM_USER_UID);
                }
            }
            User condition2 = new User();
            condition2.setUid(loginUser.getInviteCode());
            User referrer = userService.getByCondition(condition2);
            if (null == referrer) {
                return new JsonResult(10001, "邀请码不正确");
            }
            if (user.getId().equals(referrer.getId())) {
                return new JsonResult(10002, "邀请人不能是自己");
            }
            if (referrer.getStatus() == StatusType.FALSE.getCode()) {
                return new JsonResult(10002, "邀请人帐号已禁用");
            }
            if (StringUtils.isBlank(referrer.getFirstReferrer())) {
                return new JsonResult(10003, "邀请人帐号未激活");
            }
            User updateUser = new User();
            updateUser.setFirstReferrer(referrer.getId());
            updateUser.setSecondReferrer(referrer.getFirstReferrer());
            updateUser.setThirdReferrer(referrer.getSecondReferrer());
            updateUser.setLevles(referrer.getLevles() + 1);
            userService.update(user.getId(), updateUser);
            // 异步处理分组业务
            final String userId = user.getId();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        //客户端微信注册登录，用户根据系统算法分组
                        userService.updateGroupCount(userId, "");
                    } catch (Exception e) {
                        logger.error("处理分组失败！！" + e.getMessage());
                    }
                }
            }).start();
        }
        if (user.getStatus() == StatusType.FALSE.getCode()) {
            return new JsonResult(10002, "您的帐号已被禁用");
        }
        // 每次登录都会保存极光推送生成ID
        if (StringUtils.isNoneBlank(loginUser.getRegistrationId()) && !loginUser.getRegistrationId().equals(user.getRegistrationId())) {
            userService.updateRegistrationId(user.getId(), loginUser.getRegistrationId());
        }
        // 登录
        String token = TokenUtil.generateToken(user.getId(), user.getUserName(), user.getNickName());
        Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
        Sets.sadd(RedisKey.ALL_TOKENS.getKey(), token);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("user login[%s]", TokenUtil.getTokenObject(token)));
        }
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(vo, user);
        vo.setToken(token);
        vo.setScore(FormatUtils.formatDouble(user.getScore()));

        setUserVo(user, vo);
        return new JsonResult(vo);
    }

    private void setUserVo(User user, UserVo vo) throws Exception {
        if (StringUtils.isBlank(user.getPassword())) {
            vo.setIsSetPassword(StatusType.FALSE.getCode());
        }
        if (StringUtils.isBlank(user.getPayPwd())) {
            vo.setIsSetPayPwd(StatusType.FALSE.getCode());
        }
        // 获取银行列表
        List<Bank> bankList = bankService.getList();
        vo.setBankList(bankList);
        // 获取用户银行卡
        List<UserBankcard> userBankcardList = userBankcardService.getList(user.getId());
        if (userBankcardList != null && userBankcardList.size() > 0) {
            vo.setUserBankcard(userBankcardList.get(0));
        }
        // 获取用户收货地址
        List<UserAddress> userAddressList = userAddressService.getList(user.getId());
        if (userAddressList != null && userAddressList.size() > 0) {
            vo.setUserAddress(userAddressList.get(0));
        }
    }

    /**
     * 微信分享过渡页
     */
    @RequestMapping(value = "/transition", method = RequestMethod.GET)
    public ModelAndView share(HttpServletRequest request, HttpServletResponse response, ShareVo vo) throws Exception {
        logger.error("*******transition*********");
        ModelAndView mv = new ModelAndView();
        if (vo.getUid() == null) {
            mv.setViewName("error");
            mv.addObject("msg", "邀请码不能为空");
            return mv;
        }
        String shareUserId = vo.getShareUserId();
        String storeId = vo.getStoreId();
        String goodsId = vo.getGoodsId();
        if ((storeId == null || ("null").equals(storeId) || storeId.equals("undefined") || StringUtils.isEmpty(storeId)) && (goodsId == null || ("null").equals(goodsId) || goodsId.equals("undefined"))) {
            mv.setViewName("error");
            mv.addObject("msg", "该商品或者店铺已被屏蔽");
            return mv;
        }
        if (shareUserId == null || ("null").equals(shareUserId) || shareUserId.equals("undefined") || StringUtils.isEmpty(shareUserId)) {
            mv.setViewName("error");
            mv.addObject("msg", "分享用户id不能为空");
            return mv;
        }
        User condition = new User();
        condition.setUid(vo.getUid());
        User referrer = userService.getByCondition(condition);
        if (referrer == null) {
            mv.setViewName("error");
            mv.addObject("msg", "不合法的邀请码");
            return mv;
        }
        if (BrowserUtil.isWechat(request)) { // 进入微信授权页
            String redirect_uri = wechatCallbackDomain + "/user/callback";
            redirect_uri = URLEncoder.encode(redirect_uri, "utf8");
            //state组装uid+grouptype，用符号"_"分割  200828_null_2_B33BC81F04134B2EBAB6910B704CAA44_FB5937DBB58E45488D38228FC01E4928_
            String authorize = WechatApiUtil.authorizeH5(redirect_uri, WechatApiUtil.BASE_SCOPE, vo.getUid() + "_" + vo.getGroupType() + "_" + vo.getIndex() + "_" + vo.getShareUserId() + "_" + vo.getStoreId() + "_" + vo.getGoodsId());
            logger.error("**************************");
            logger.error(authorize);
            logger.error("**************************");
            if (StringUtils.isNoneBlank(authorize)) {
                response.sendRedirect(authorize);
                return null;
            }
        }
        // 进入提示页面

        mv.setViewName("redirect:" + PAGE_SHARE + "?index=1");
        return mv;
    }

    /**
     * 微信分享回调
     */
    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public ModelAndView share(HttpServletRequest request, String code, String state) throws Exception {
        logger.error("*******callback*********");
        ModelAndView mv = new ModelAndView();
        JSONObject json = WechatApiUtil.accessTokenH5(code, state);
        if (json == null || !json.has("subscribe")) {
            mv.setViewName("error");
            mv.addObject("msg", "获取您的公开信息（昵称、头像等）失败");
            return mv;
        }
        logger.error("****************json**********************");
        logger.error("**************************************");
        logger.error(com.alibaba.fastjson.JSONObject.toJSONString(json));
        logger.error(WechatApiUtil.getJsonByKey(json, "openid"));
        logger.error("**************************************");
        logger.error("**************************************");
        String unionid = WechatApiUtil.getJsonByKey(json, "unionid");
        String openid = WechatApiUtil.getJsonByKey(json, "openid");
        String nickName = "";
        String headImgUrl = "";
//        if (json.getInt("subscribe") == 1) {
        nickName = WechatApiUtil.getJsonByKey(json, "nickname");
        nickName = nickName.replaceAll("[^\u0000-\uFFFF]", "?"); // 过滤非UTF-8字符集,用"?"代替，如Emoji表情
        headImgUrl = WechatApiUtil.getJsonByKey(json, "headimgurl");
//        }
        User user = new User();
        user.setWeixin(unionid);
        user = userService.getByCondition(user);

        if (null == user) {
            user = new User();
            user.setOpenId(openid);
            user.setLoginTime(new Date());
            user.setWeixin(unionid);
            nickName = EmojiUtil.replaceEmoji(nickName);
            user.setNickName(nickName);
            user.setHeadImgUrl(headImgUrl);
            if (ToolUtil.isEmpty(headImgUrl)) {
                user.setHeadImgUrl(USER_LOGO);
            }
            userService.add(user);
        }
        User tem = new User();
        tem.setOpenId(openid);
        tem.setLoginTime(new Date());
        userService.update(user.getId(), tem);
        //根据符号"_"分割出uid和grouptype
        String groupType = "";
        String refferUid = "";
        String index = "";
        String shareUserId = "";
        String storeId = "";
        String goodsId = "";
        String[] strSplit = state.split("_");
        if (strSplit.length > 1) {
            refferUid = strSplit[0];
            groupType = strSplit[1];
            index = strSplit[2];
            shareUserId = strSplit[3];
            storeId = strSplit[4];
            goodsId = strSplit[5];
            if (groupType == null || ("null").equals(groupType) || groupType.equals("undefined") || StringUtils.isEmpty(groupType)) {
                groupType = "";
            }
            if (index == null || ("null").equals(index) || index.equals("undefined") || StringUtils.isEmpty(index)) {
                index = "1";
            }
        } else {
            refferUid = strSplit[0];
            index = "1";
        }
        if ((storeId == null || ("null").equals(storeId) || storeId.equals("undefined") || StringUtils.isEmpty(storeId)) && (goodsId == null || ("null").equals(goodsId) || goodsId.equals("undefined"))) {
            mv.setViewName("error");
            mv.addObject("msg", "该商品或者店铺已被屏蔽");
            return mv;
        }
        if (shareUserId == null || ("null").equals(shareUserId) || shareUserId.equals("undefined") || StringUtils.isEmpty(shareUserId)) {
            mv.setViewName("error");
            mv.addObject("msg", "分享用户id不能为空");
            return mv;
        }
        if (StringUtils.isBlank(user.getFirstReferrer())) {
            User condition = new User();
            condition.setUid(Integer.parseInt(refferUid));
            //推荐人（一度人脉）
            User referrer = userService.getByCondition(condition);

            if (null == referrer) {
                mv.setViewName("error");
                mv.addObject("msg", "邀请码不正确");
            }
            if (user.getId().equals(referrer.getId())) {
                mv.setViewName("error");
                mv.addObject("msg", "邀请人不能是自己");
            }
            if (referrer.getStatus() == StatusType.FALSE.getCode()) {
                mv.setViewName("error");
                mv.addObject("msg", "邀请人帐号已禁用");
            }
            if (StringUtils.isBlank(referrer.getFirstReferrer())) {
                mv.setViewName("error");
                mv.addObject("msg", "邀请人帐号未激活");
            }
            //推荐人的推荐人（二级人脉）
            User temp = new User();
            temp.setFirstReferrer(referrer.getId());
            temp.setSecondReferrer(referrer.getFirstReferrer());
            temp.setThirdReferrer(referrer.getSecondReferrer());
            temp.setLevles(referrer.getLevles() + 1);
            temp.setOpenId(openid);
            temp.setLoginTime(new Date());
            userService.update(user.getId(), temp);

            // 异步处理分组业务
            final String userId = user.getId();
            final String finalGroupType = groupType;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        //指定分组
                        userService.updateGroupCount(userId, finalGroupType);
                    } catch (Exception e) {
                        logger.error("处理分组失败！！" + e.getMessage());
                    }
                }
            }).start();
        }
        // 登录
        String token = TokenUtil.generateToken(user.getId(), user.getUserName(), user.getNickName());
        Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
        Sets.sadd(RedisKey.ALL_TOKENS.getKey(), token);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("user login[%s]", TokenUtil.getTokenObject(token)));
        }
        /*UserVo vo = new UserVo();
        BeanUtils.copyProperties(vo, user);
		vo.setToken(token);
		vo.setScore(FormatUtils.formatDouble(user.getScore()));

		setUserVo(user, vo);*/
        // 进入h5页面
        mv.addObject("info", "进入h5页面");
        mv.setViewName("redirect:" + h5page + "?uid=" + refferUid + "&token=" + token + "&shareUserId=" + shareUserId + "&storeId=" + storeId + "&goodsId=" + goodsId + "&index=" + index + "&newUid=" + user.getUid() + "&newUserId=" + user.getId());
        return mv;
    }


    /**
     * 微信分享过渡页(兼容微信店铺版)
     */
    @RequestMapping(value = "/wdtransition", method = RequestMethod.GET)
    public ModelAndView weixinStoreTransition(HttpServletRequest request, HttpServletResponse response, ShareVo vo) throws Exception {
        logger.error("**********************************wdtransition*********************************");
        logger.error("*******vo *********" + JSON.toJSONString(vo));
        logger.error("**********************************wdtransition*********************************");

        ModelAndView mv = new ModelAndView();
        String storeId = vo.getStoreId();
        if (storeId == null || ("null").equals(storeId) || storeId.equals("undefined") || StringUtils.isEmpty(storeId)) {
            mv.setViewName("error");
            mv.addObject("msg", "店铺id不能为空");
            return mv;
        }
        Store store = storeService.getById(storeId);
        if (store == null) {
           /* mv.setViewName("error");
            mv.addObject("msg", "店铺已被屏蔽");
            return mv;*/
            return new ModelAndView(new RedirectView(ERROR_PAGE));
        }
        if (ToolUtil.isEmpty(store.getMenuUrl())) {
          /*  mv.setViewName("error");
            mv.addObject("msg", "店铺尚未设置链接");
            return mv;*/
            return new ModelAndView(new RedirectView(ERROR_PAGE));
        }
        if (vo.getUid() == null) {
            User condition = new User();
            condition.setStoreId(storeId);
            User storeUser = userService.getByCondition(condition);
            if (storeUser == null) {
              /*  mv.setViewName("error");
                mv.addObject("msg", "创建店铺用户已被屏蔽");
                return mv;*/
                return new ModelAndView(new RedirectView(ERROR_PAGE));
            }
            vo.setUid(storeUser.getUid());
        }
        if (BrowserUtil.isWechat(request)) { // 进入微信授权页
            String redirect_uri = wechatCallbackDomain + "/user/wxstorecallback";
            redirect_uri = URLEncoder.encode(redirect_uri, "utf8");
            //state组装uid+grouptype，用符号"_"分割  200828_null_2_FB5937DBB58E45488D38228FC01E4928_
            String authorize = WechatApiUtil.authorizeH5(redirect_uri, WechatApiUtil.BASE_SCOPE, vo.getUid() + "_" + vo.getGroupType() + "_" + vo.getIndex() + "_" + vo.getStoreId() + "_" + vo.getGoodsId() + "_" + vo.getShareUrl());
            logger.error("************00000000**************");
            logger.error(wechatCallbackDomain);
            logger.error(authorize);
            logger.error("**************************");
            if (StringUtils.isNoneBlank(authorize)) {
                response.sendRedirect(authorize);
                return null;
            }
        }
        return null;
    }

    /**
     * 微信分享回调
     */
    @RequestMapping(value = "/wxstorecallback", method = RequestMethod.GET)
    public ModelAndView weixinStoreCallBack(HttpServletRequest request, String code, String state) throws Exception {
        logger.error("*******callback*********");
        ModelAndView mv = new ModelAndView();
        JSONObject json = WechatApiUtil.accessTokenH5(code, state);
        if (json == null || !json.has("subscribe")) {
            mv.setViewName("error");
            mv.addObject("msg", "获取您的公开信息（昵称、头像等）失败");
            return mv;
        }
        logger.error("****************json**********************");
        logger.error("**************************************");
        logger.error(com.alibaba.fastjson.JSONObject.toJSONString(json));
        logger.error(WechatApiUtil.getJsonByKey(json, "openid"));
        logger.error("**************************************");
        logger.error("**************************************");
        String unionid = WechatApiUtil.getJsonByKey(json, "unionid");
        String openid = WechatApiUtil.getJsonByKey(json, "openid");
        String nickName = "";
        String headImgUrl = "";
        if (json.getInt("subscribe") == 1) {
            nickName = WechatApiUtil.getJsonByKey(json, "nickname");
            nickName = nickName.replaceAll("[^\u0000-\uFFFF]", "?"); // 过滤非UTF-8字符集,用"?"代替，如Emoji表情
            headImgUrl = WechatApiUtil.getJsonByKey(json, "headimgurl");
        }
        User user = new User();
        user.setWeixin(unionid);
        user = userService.getByCondition(user);

        if (null == user) {
            user = new User();
            user.setOpenId(openid);
            user.setLoginTime(new Date());
            user.setWeixin(unionid);
            nickName = EmojiUtil.replaceEmoji(nickName);
            user.setNickName(nickName);
            user.setHeadImgUrl(headImgUrl);
            if (ToolUtil.isEmpty(headImgUrl)) {
                user.setHeadImgUrl(USER_LOGO);
            }
            userService.add(user);
        }

        User tem = new User();
        tem.setOpenId(openid);
        tem.setLoginTime(new Date());
        userService.update(user.getId(), tem);
        //根据符号"_"分割出uid和grouptype
        String groupType = "";
        String refferUid = "";
        String index = "";
        String shareUserId = "";
        String storeId = "";
        String goodsId = "";
        String shareUrl = "";
        String[] strSplit = state.split("_");
        if (strSplit.length > 1) {
            refferUid = strSplit[0];
            groupType = strSplit[1];
            index = strSplit[2];
            storeId = strSplit[3];
            goodsId = strSplit[4];
            shareUrl = strSplit[5];
            if (groupType == null || ("null").equals(groupType) || groupType.equals("undefined") || StringUtils.isEmpty(groupType)) {
                groupType = "";
            }
            if (index == null || StringUtils.isEmpty(index)) {
                index = "Im";
            }
        } else {
            refferUid = strSplit[0];
            index = "Im";
        }
        if ((storeId == null || ("null").equals(storeId) || storeId.equals("undefined") || StringUtils.isEmpty(storeId)) && (goodsId == null || ("null").equals(goodsId) || goodsId.equals("undefined"))) {
            mv.setViewName("error");
            mv.addObject("msg", "该商品或者店铺已被屏蔽");
            return mv;
        }
        if (StringUtils.isBlank(user.getFirstReferrer())) {
            User condition = new User();
            condition.setUid(Integer.parseInt(refferUid));
            //推荐人（一度人脉）
            User referrer = userService.getByCondition(condition);

            if (null == referrer) {
                mv.setViewName("error");
                mv.addObject("msg", "邀请码不正确");
                return mv;
            }
            if (user.getId().equals(referrer.getId())) {
                mv.setViewName("error");
                mv.addObject("msg", "邀请人不能是自己");
                return mv;
            }
            if (referrer.getStatus() == StatusType.FALSE.getCode()) {
                mv.setViewName("error");
                mv.addObject("msg", "邀请人帐号已禁用");
                return mv;
            }
            if (StringUtils.isBlank(referrer.getFirstReferrer())) {
                mv.setViewName("error");
                mv.addObject("msg", "邀请人帐号未激活");
                return mv;
            }
            //推荐人的推荐人（二级人脉）
            User temp = new User();
            temp.setFirstReferrer(referrer.getId());
            temp.setSecondReferrer(referrer.getFirstReferrer());
            temp.setThirdReferrer(referrer.getSecondReferrer());
            temp.setLevles(referrer.getLevles() + 1);
            temp.setOpenId(openid);
            temp.setLoginTime(new Date());
            userService.update(user.getId(), temp);

            // 异步处理分组业务
            final String userId = user.getId();
            final String finalGroupType = groupType;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        //指定分组
                        userService.updateGroupCount(userId, finalGroupType);
                    } catch (Exception e) {
                        logger.error("处理分组失败！！" + e.getMessage());
                    }
                }
            }).start();
        }
        // 登录
        String token = TokenUtil.generateToken(user.getId(), user.getUserName(), user.getNickName());
        logger.error("999999999999999999999999999999999999999999999999999999999999999999999999999999");
        logger.error("999999999999999999999999999999999999999999999999999999999999999999999999999999");
        logger.error("token " + token);
        logger.error("999999999999999999999999999999999999999999999999999999999999999999999999999999");
        logger.error("999999999999999999999999999999999999999999999999999999999999999999999999999999");
        Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
        Sets.sadd(RedisKey.ALL_TOKENS.getKey(), token);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("user login[%s]", TokenUtil.getTokenObject(token)));
        }

        Store store = storeService.getById(storeId);
        if (store == null || ToolUtil.isEmpty(store.getMenuUrl())) {
            mv.setViewName("error");
            mv.addObject("msg", "店铺信息尚未完善");
            return mv;
        }
        if (store.getWeixinStatus() == null || store.getWeixinStatus() != 2) {
            return new ModelAndView(new RedirectView(ERROR_PAGE));
        }
        /*UserVo vo = new UserVo();
        BeanUtils.copyProperties(vo, user);
		vo.setToken(token);
		vo.setScore(FormatUtils.formatDouble(user.getScore()));
		setUserVo(user, vo);*/
//        Map<String, String> map = (Map<String, String>) ToolUtil.getJavaObject(store.getMenuUrl(), Map.class);
        logger.error("===============================================");
        logger.error("wxstorecallback state：" + state);
        logger.error("分享的linkurl：" + shareUrl);
        if (ToolUtil.isNotEmpty(shareUrl)) {
            shareUrl += "?" + "storeId=" + storeId + "&index=" + index + "&uid=" + refferUid;
            return new ModelAndView(new RedirectView(shareUrl));
        }
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(store.getMenuUrl());
        /*if (!jsonObject.containsKey(index)) {
            *//*mv.setViewName("error");
            mv.addObject("msg", "店铺参数配置错误");
            return mv;*//*
            return  new ModelAndView(new RedirectView("http://doupaimall.com/wxpage?index=errorPage"));
        }*/
        // 进入h5页面
        mv.addObject("info", "微信店铺");
        String url = (String) jsonObject.get(index);

        if (index.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        String redirectUrl = url + "?uid=" + refferUid + "&token=" + token + "&storeId=" + storeId + "&goodsId=" + goodsId + "&index=" + index + "&newUid=" + user.getUid() + "&newUserId=" + user.getId();
        return new ModelAndView(new RedirectView(redirectUrl));
    }


    /**
     * App下载页面
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView test(HttpServletRequest request, String uid) throws Exception {
        ModelAndView mv = new ModelAndView();
        // 进入下载页
        ParamUtil util = ParamUtil.getIstance();
        mv.setViewName("test");
        mv.addObject("uid", uid);
        mv.addObject("androidAppUrl", util.get(Parameter.ANDROIDAPPURL, "javascript:void(0);"));
        mv.addObject("iosAppUrl", util.get(Parameter.IOSAPPURL, "javascript:void(0);"));
        return mv;
    }


    /*
     * 根据token获取用户信息
     */
    @ResponseBody
    @RequestMapping(value = "/login/getUser", method = RequestMethod.GET)
    public JsonResult getUser(HttpServletRequest request) throws Exception {
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
        // Redis获取Token
        String redisToken = Strings.get(RedisKey.TOKEN_API.getKey() + token.getId());
        if (logger.isInfoEnabled()) {
            logger.info(String.format("user again login[%s]", TokenUtil.getTokenObject(redisToken)));
        }
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(vo, user);
        vo.setToken(redisToken);
        vo.setScore(FormatUtils.formatDouble(user.getScore()));

        setUserVo(user, vo);
        return new JsonResult(vo);
    }


    public static String getGroupType() {
        return group.get(RandomUtil.randomInt(0, 3));
    }

    public static void main(String[] args) {
        String url1 = "http://www?a=rr";
        url1 = url1.substring(0, url1.indexOf("?"));
        System.out.println(url1);
        List<String> noAuthorized = new ArrayList<String>();
        noAuthorized.add("/m/share/transition");
        System.out.println("/m/share/transition".contains("/m/share/transition"));
        System.out.println(noAuthorized.contains("/m/share/transition"));
//        String url="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        String accessToken = WechatApiUtil.accessTokenForAPi();
//         ouH4bs_JG0XvtFfvLQZUDnWHPCl0
        accessToken = "TGLXGm_2q9j346vIl7eh6kEyE8izjZH3XL3y-IUeJP9Kma7BxD2ro6QUV8qs2sC6xZ4nEmBpxJzC2X9JeXBYKvqMPBBJ9sat3emuXljMu5NkIkGjRL7Ky-7cOQp488VcMTIbAAAAHW";
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=ouH4bs_JG0XvtFfvLQZUDnWHPCl0&lang=zh_CN";
        String url2 = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=ouH4bs_JG0XvtFfvLQZUDnWHPCl0&lang=zh_CN";
        JSONObject json = HttpsUtil.doGetRequest(url);
        System.out.println(json.containsKey("unionid"));
        System.out.println(json.toString());
    }
}
