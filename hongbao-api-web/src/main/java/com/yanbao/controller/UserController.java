package com.yanbao.controller;

import com.mall.model.*;
import com.taobao.api.internal.util.Base64;
import com.yanbao.constant.GradeType;
import com.yanbao.constant.RedisKey;
import com.yanbao.constant.ResultCode;
import com.yanbao.constant.StatusType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.redis.Sets;
import com.yanbao.redis.Strings;
import com.yanbao.service.*;
import com.yanbao.util.*;
import com.yanbao.util.qrcode.QRCodeUtil;
import com.yanbao.vo.*;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
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
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final Integer SYSTEM_USER_UID = 200000;
    public static List<String> group = new ArrayList<>();

    @Value("${environment}")
    private String environment;

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
    @Autowired
    private FileService fileService;
    @Value("${api_domain}")
    private String apiDomain;
    @Value("${wechat_callback_domain}")
    private String wechatCallbackDomain;
    @Value("${file_root_path}")
    private String FILE_ROOT_PATH;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private WalletSignService walletSignService;
    @Autowired
    private StoreService storeService;

    /**
     * 微信登录
     */
    @ResponseBody
    @RequestMapping(value = "/token", method = RequestMethod.GET)
    public JsonResult loginByWeixin(HttpServletRequest request, String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            return new JsonResult(-1, "id不能為空");
        }
        User condition = new User();
        condition.setId(id);
        User user = userService.getByCondition(condition);
        // 登录
        String token = TokenUtil.generateToken(user.getId(), "", user.getNickName());
        Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("user login[%s]", TokenUtil.getTokenObject(token)));
        }
        user.setRemark(token);
        return new JsonResult(user);
    }


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
                if (StatusType.TRUE.getCode().equals(ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.INVITESWICH), 1))) {
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
            updateUser.setLoginTime(new Date());
            updateUser.setUpdateTime(new Date());
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
        User updateUser = new User();
        updateUser.setLoginTime(new Date());
        updateUser.setUpdateTime(new Date());
        userService.update(user.getId(), updateUser);

        // 登录
        String token = TokenUtil.generateToken(user.getId(), user.getUserName(), user.getNickName());
        loginRedis(user, token);
        Sets.sadd(RedisKey.ALL_TOKENS.getKey(), token);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("user login[%s]", TokenUtil.getTokenObject(token)));
        }
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(vo, user);
        vo.setToken(token);
        vo.setScore(FormatUtils.formatDouble(user.getScore()));
        if (ToolUtil.isEmpty(user.getOldUnionId())) {
            vo.setIsBindSHWeiXin(StatusType.TRUE.getCode());
        } else {
            vo.setIsBindSHWeiXin(StatusType.FALSE.getCode());
        }
        if (ToolUtil.isEmpty(user.getAppOpenId())) {
            vo.setIsBindAppOpenId(StatusType.TRUE.getCode());
        } else {
            vo.setIsBindAppOpenId(StatusType.FALSE.getCode());
        }

        setUserVo(user, vo);
        return new JsonResult(vo);
    }


    /**
     * 手机号注册
     */
    @ResponseBody
    @RequestMapping(value = "/registerwithphone", method = RequestMethod.POST)
    public JsonResult registerWithPhone(HttpServletRequest request, @RequestBody RegisterVo vo) throws Exception {
        if (StringUtils.isBlank(vo.getPhone())) {
            return new JsonResult(0, "请输入手机号");
        }
        if (StringUtils.isBlank(vo.getSmsCode())) {
            return new JsonResult(1, "请输入短信验证码");
        }
        String smsCode2 = Strings.get(RedisKey.SMS_CODE.getKey() + vo.getPhone());
        if (!vo.getSmsCode().equalsIgnoreCase(smsCode2)) {
            return new JsonResult(2, "短信验证码不正确或已失效");
        }
        User condition = new User();
        condition.setPhone(vo.getPhone());
        User user = userService.getByCondition(condition);
        if (user != null) {
            return new JsonResult(-1, "该手机号已经注册");
        }
        if (StringUtils.isBlank(vo.getPassword())) {
            return new JsonResult(1, "请输入登录密码");
        }
        if (StringUtils.isBlank(vo.getPasswordConfirm())) {
            return new JsonResult(3, "请输入确认登录密码");
        }
        if (!vo.getPassword().equals(vo.getPasswordConfirm())) {
            return new JsonResult(7, "两次输入密码不一致");
        }
        if (ToolUtil.isEmpty(vo.getNickName())) {
            return new JsonResult(8, "昵称不能为空");
        }
        User referrerCondition = new User();
        referrerCondition.setUid(vo.getInviteCode());
        User referrer = userService.getByCondition(referrerCondition);
        if (null == referrer) {
            return new JsonResult(10001, "邀请码不正确");
        }
        if (referrer.getUid() == vo.getInviteCode()) {
            return new JsonResult(10002, "邀请人不能是自己");
        }
        if (referrer.getStatus() == StatusType.FALSE.getCode()) {
            return new JsonResult(10002, "邀请人帐号已禁用");
        }
        if (StringUtils.isBlank(referrer.getFirstReferrer())) {
            return new JsonResult(10003, "邀请人帐号未激活");
        }
        User addUser = new User();
        addUser.setPhone(vo.getPhone());
        addUser.setPassword(vo.getPassword());
        addUser.setFirstReferrer(referrer.getId());
        addUser.setSecondReferrer(referrer.getFirstReferrer());
        addUser.setThirdReferrer(referrer.getSecondReferrer());
        addUser.setLevles(referrer.getLevles() + 1);
        addUser.setUpdateTime(new Date());
        addUser.setLoginTime(new Date());
        addUser.setHeadImgUrl("http://user.doupaimall.com/logo.png");
        addUser.setStatus(StatusType.TRUE.getCode());
        addUser.setNickName(vo.getNickName());
        // 每次都会保存极光推送生成ID
        if (StringUtils.isNoneBlank(vo.getRegistrationId())) {
            addUser.setRegistrationId(vo.getRegistrationId());
        }
        userService.add(addUser);
        user = userService.getByCondition(condition);
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

        // 登录
        String token = TokenUtil.generateToken(user.getId(), user.getUserName(), user.getNickName());
        loginRedis(user, token);
        Sets.sadd(RedisKey.ALL_TOKENS.getKey(), token);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("user login[%s]", TokenUtil.getTokenObject(token)));
        }
        UserVo resvo = new UserVo();
        BeanUtils.copyProperties(vo, user);
        resvo.setToken(token);
        resvo.setScore(FormatUtils.formatDouble(user.getScore()));
        setUserVo(user, resvo);
        return new JsonResult(resvo);
    }


    /**
     * 绑定新的微信号
     */
    @ResponseBody
    @RequestMapping(value = "/bindweixin", method = RequestMethod.POST)
    public JsonResult bindWeixin(HttpServletRequest request, @RequestBody BindWeiXinVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(1, "用户登录失效");
        }
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(vo.getUnionId())) {
            return new JsonResult(0, "微信号不能为空");
        }

        User conditon = new User();
        conditon.setWeixin(vo.getUnionId());
        User oldWeiXinUser = userService.getByCondition(conditon);
        if (oldWeiXinUser != null) {
            if (ToolUtil.isNotEmpty(oldWeiXinUser.getOldUnionId()) && ToolUtil.isNotEmpty(oldWeiXinUser.getPhone())) {
                return new JsonResult(0, "此微信号已被其他账号绑定");
            }
            User updateOldeWxModel = new User();
            updateOldeWxModel.setWeixin(oldWeiXinUser.getWeixin() + "200000");
            updateOldeWxModel.setRemark(DateTimeUtil.formatDate(new Date(), DateTimeUtil.PATTERN_LONG) + ",新用户同一个微信号重复注册,将此微信号做变更");
            updateOldeWxModel.setStatus(StatusType.FALSE.getCode());
            userService.update(oldWeiXinUser.getId(), updateOldeWxModel);
        }

        User updateModel = new User();
        updateModel.setWeixin(vo.getUnionId());
        if (ToolUtil.isEmpty(user.getWeixin())) {
            updateModel.setRemark(DateTimeUtil.formatDate(new Date(), DateTimeUtil.PATTERN_LONG) + ",新用户绑定微信号");
        } else {
            updateModel.setRemark(DateTimeUtil.formatDate(new Date(), DateTimeUtil.PATTERN_LONG) + ",老用户切换微信号");
            updateModel.setOldUnionId(user.getWeixin());
        }
        if (ToolUtil.isNotEmpty(vo.getHeadImgUrl())) {
            updateModel.setHeadImgUrl(vo.getHeadImgUrl());
        }
        if (ToolUtil.isNotEmpty(vo.getNickName())) {
            updateModel.setNickName(vo.getNickName());
        }
        if (vo.getSex() != null) {
            updateModel.setSex(vo.getSex());
        }
        userService.update(user.getId(), updateModel);
        return new JsonResult();
    }


    private void loginRedis(User user, String token) {
        String redisToken = Strings.get(RedisKey.TOKEN_API.getKey() + user.getId());
        Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
        //正式環境不支持多點登錄
        /*if("online".equalsIgnoreCase(environment.trim())) {
            String redisToken = Strings.get(RedisKey.TOKEN_API.getKey() + user.getId());
            Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
            //測試環境支持商家多點登錄
        }else if("test".equalsIgnoreCase(environment.trim())) {
            String redisToken = Strings.get(RedisKey.TOKEN_API.getKey() + user.getId());
            if (StringUtils.isEmpty(redisToken) || StringUtils.isEmpty(user.getStoreId())) {
                Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
                return;
            }
            if (!StringUtils.isEmpty(redisToken) && !StringUtils.isEmpty(user.getStoreId())) {
                SortSet.zadd(RedisKey.TOKEN_API_STORE.getKey() + user.getId(), System.currentTimeMillis(), token);
                Sets.expireSetSecond(RedisKey.TOKEN_API_STORE.getKey() + user.getId(), RedisKey.TOKEN_API_STORE.getSeconds());
            }
        }*/
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
        if (ToolUtil.isEmpty(user.getWeixin())) {
            vo.setIsBindWeixin(StatusType.FALSE.getCode());
        }
        if (ToolUtil.isEmpty(user.getWeixin())) {
            vo.setIsBindWeixin(StatusType.FALSE.getCode());
        }
        Boolean isCompleteInfo = userService.isCompleteInfo(user);
        if (isCompleteInfo) {
            vo.setIsCompleteInfo(StatusType.TRUE.getCode());
        }
    }

    /**
     * 编辑用户基本信息
     */
    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public JsonResult update(HttpServletRequest request, @RequestBody UserVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        // 更新用户信息
        User user = new User();
        BeanUtils.copyProperties(user, vo);
        userService.update(token.getId(), user);
        // 更新用户银行卡信息
        UserBankcard bankcard = vo.getUserBankcard();
        List<UserBankcard> bankcardList = userBankcardService.getList(token.getId());
        if (bankcardList != null && bankcardList.size() > 0) {
            userBankcardService.update(bankcardList.get(0).getId(), bankcard);
        } else {
            bankcard.setUserId(token.getId());
            userBankcardService.add(bankcard);
        }
        // 更新用户收货地址
        UserAddress address = vo.getUserAddress();
        List<UserAddress> addressList = userAddressService.getList(token.getId());
        if (addressList != null && addressList.size() > 0) {
            userAddressService.update(addressList.get(0).getId(), address);
        } else {
            address.setUserId(token.getId());
            userAddressService.add(address);
        }
        return new JsonResult(vo);
    }

    /**
     * 编辑用户头像
     */
    @ResponseBody
    @RequestMapping(value = "/edit/headImg", method = RequestMethod.POST)
    public JsonResult updateHeadImg(HttpServletRequest request, @RequestBody ImageVo imageVo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);

        if (imageVo == null || StringUtils.isBlank(imageVo.getPath())) {
            return new JsonResult(1, "请上传头像");
        }
        User user = new User();
        user.setHeadImgUrl(imageVo.getPath());
        userService.update(token.getId(), user);
        return new JsonResult();
    }

    /**
     * 设置/修改支付密码
     */
    @ResponseBody
    @RequestMapping(value = "/change/payPwd", method = RequestMethod.POST)
    public JsonResult changePayPwd(HttpServletRequest request, @RequestBody PayPwdVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (StringUtils.isNotBlank(user.getPayPwd())) {
            if (StringUtils.isBlank(vo.getOldPayPwd())) {
                return new JsonResult(1, "请输入当前支付密码");
            }
            if (!user.getPayPwd().equals(Md5Util.MD5Encode(vo.getOldPayPwd(), user.getSalt()))) {
                return new JsonResult(2, "当前支付密码错误");
            }
        }
        if (StringUtils.isBlank(vo.getNewPayPwd())) {
            return new JsonResult(3, "请输入新支付密码");
        }
        if (vo.getNewPayPwd().equals(vo.getOldPayPwd())) {
            return new JsonResult(4, "新旧支付密码不能相同");
        }
        if (vo.getNewPayPwd().length() != 6 || !PasswordUtil.isAllNumeric(vo.getNewPayPwd())) {
            return new JsonResult(5, "支付密码必须为6纯数字");
        }
        if (PasswordUtil.isAllEqualStr(vo.getNewPayPwd())) {
            return new JsonResult(6, "支付密码必须使用多个数字组合");
        }
        if (!vo.getNewPayPwd().equals(vo.getNewPayPwd2())) {
            return new JsonResult(7, "两次输入新支付密码不一致");
        }
        // 更新用户支付密码
        User model = new User();
        model.setPayPwd(Md5Util.MD5Encode(vo.getNewPayPwd(), user.getSalt()));
        userService.update(token.getId(), model);
        return new JsonResult();
    }

    /**
     * 绑定手机号
     */
    @ResponseBody
    @RequestMapping(value = "/change/phone", method = RequestMethod.POST)
    public JsonResult changePhone(HttpServletRequest request, @RequestBody PhoneVo vo) {
        Token token = TokenUtil.getSessionUser(request);
        if (StringUtils.isBlank(vo.getPhone())) {
            return new JsonResult(0, "请输入手机号");
        }
        if (StringUtils.isBlank(vo.getSmsCode())) {
            return new JsonResult(1, "请输入短信验证码");
        }
        String smsCode2 = Strings.get(RedisKey.SMS_CODE.getKey() + vo.getPhone());
        if (!vo.getSmsCode().equalsIgnoreCase(smsCode2)) {
            return new JsonResult(2, "短信验证码不正确或已失效");
        }
        User condition = new User();
        condition.setPhone(vo.getPhone());
        User temp;
        User user;
        try {
            temp = userService.getByCondition(condition);
            if (temp != null && !temp.getId().equals(token.getId())) {
                return new JsonResult(3, "该手机号已注册");
            }
            user = userService.getById(token.getId());
            if (null == user) {
                logger.error(String.format("Illegal user id[%s]", token.getId()));
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            return new JsonResult(4, "网络异常");
        }
        try {
            // 更新用户手机号
            User model = new User();
            model.setPhone(vo.getPhone());
            userService.update(token.getId(), model);
            Strings.del(RedisKey.SMS_CODE.getKey() + vo.getPhone());
        } catch (Exception e1) {
            e1.printStackTrace();
            return new JsonResult(5, "绑定手机号失败");
        }
        try {
            userService.updateUserPhoneSendEp(user, vo.getPhone(), token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return new JsonResult(5, "EP赠送活动已结束");
        }
        return new JsonResult();
    }

    /**
     * 更改手机号
     */
    @ResponseBody
    @RequestMapping(value = "/update/phone", method = RequestMethod.POST)
    public JsonResult updatePhone(HttpServletRequest request, @RequestBody PhoneVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(1, "用户登录失效");
        }
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(vo.getPhone())) {
            return new JsonResult(0, "请输入手机号");
        }
        if (StringUtils.isEmpty(user.getPhone())) {
            return new JsonResult(1, "请先完善资料");
        }
        if (StringUtils.isBlank(vo.getSmsCode())) {
            return new JsonResult(2, "请输入短信验证码");
        }
        String smsCode2 = Strings.get(RedisKey.SMS_CODE.getKey() + vo.getPhone());
        if (!vo.getSmsCode().equalsIgnoreCase(smsCode2)) {
            return new JsonResult(3, "短信验证码不正确或已失效");
        }
        User condition = new User();
        condition.setPhone(vo.getPhone());
        User temp;
        try {
            temp = userService.getByCondition(condition);
            if (temp != null && !temp.getId().equals(token.getId())) {
                return new JsonResult(4, "该手机号已注册");
            }
        } catch (Exception e) {
            return new JsonResult(5, "网络异常");
        }
        try {
            // 更新用户手机号
            User model = new User();
            model.setPhone(vo.getPhone());
            userService.update(token.getId(), model);
            Strings.del(RedisKey.SMS_CODE.getKey() + vo.getPhone());
        } catch (Exception e1) {
            e1.printStackTrace();
            return new JsonResult(6, "更改手机号失败");
        }
        return new JsonResult();
    }


    /**
     * 绑定手机号
     */
    @ResponseBody
    @RequestMapping(value = "/phone", method = RequestMethod.GET)
    public JsonResult changePhone(HttpServletRequest request, String phone, String userId) {

        User user = null;
        try {
            user = userService.getById(userId);
            User update = new User();
            update.setPhone(phone);
            userService.update(userId, update);
            user = userService.getById(userId);
            userService.updateUserPhoneSendEp(user, phone, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult();
    }

    /**
     * 绑定提现微信openid app商户号对应的
     */
    @ResponseBody
    @RequestMapping(value = "/bindappopenid", method = RequestMethod.POST)
    public JsonResult bindAppOpenId(HttpServletRequest request, @RequestBody BindOpenIdVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(1, "用户登录失效");
        }
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (ToolUtil.isEmpty(vo.getAppOpenId())){
            return new JsonResult(1, "获取微信提现账号失败");
        }
        try {
            User update = new User();
            update.setAppOpenId(vo.getAppOpenId());
            userService.update(user.getId(), update);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(-1,"绑定微信失败");
        }
        return new JsonResult();
    }


    /**
     * 获取用户积分
     */
    @ResponseBody
    @RequestMapping(value = "/score", method = RequestMethod.GET)
    public JsonResult userScore(HttpServletRequest request) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        Map<String, Object> result = new HashMap<String, Object>();
        // 获取积分
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        result.put("score", FormatUtils.formatDouble(user.getScore()));
        result.put("exchangeEP", FormatUtils.formatDouble(user.getExchangeEP()));
        result.put("doudou", FormatUtils.formatDouble(user.getDoudou()));
        // 获取赠送、兑换开关状态
        result.put("donateSwich", ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.DONATESWICH), 0));
        result.put("exchangeSwitch", ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.EXCHANGESWITCH), 0));
        return new JsonResult(result);
    }

    /**
     * 获取用户一度人脉列表
     */
    @ResponseBody
    @RequestMapping(value = "/contacts", method = RequestMethod.GET)
    public JsonResult contacts(HttpServletRequest request, Page page) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User condition = new User();
        condition.setFirstReferrer(token.getId());
        condition.setStatus(StatusType.TRUE.getCode());
        PageResult<User> result = userService.getPage(condition, page);
        List<User> rows = result.getRows();
        if (rows == null || rows.size() < 0) {
            return new JsonResult(result);
        }
        List<Map<String, Object>> rows2 = new ArrayList<Map<String, Object>>();
        for (User model : rows) {
            Map<String, Object> temp = new HashMap<String, Object>();
            temp.put("uid", model.getUid());//账号
            temp.put("nickName", model.getNickName());//姓名
            temp.put("headImgUrl", model.getHeadImgUrl());//头像
            temp.put("createTime", model.getCreateTime());//注册时间
            temp.put("remark", model.getRemark());//备注
            temp.put("groupType", model.getGroupType());//分组类型
            temp.put("grade", model.getGrade());//合伙人等级
            boolean isflag = userService.isCompleteInfo(model);
            temp.put("isComplete", isflag);//资料完善要求绑定手机号码、设置地址信息、设置登录密码、设置支付密码。
            rows2.add(temp);
        }
        PageResult<Map<String, Object>> result2 = new PageResult<Map<String, Object>>(result.getPageNo(), result.getPageSize(), result.getTotalSize(), rows2);
        return new JsonResult(result2);
    }

    /**
     * 用户二三度人脉统计及推荐人信息
     */
    @ResponseBody
    @RequestMapping(value = "/contacts/count", method = RequestMethod.GET)
    public JsonResult contactsCount(HttpServletRequest request) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        Map<String, Object> result = new HashMap<String, Object>();
        // 一度人脉统计
        User condition1 = new User();
        condition1.setFirstReferrer(token.getId());
        condition1.setStatus(StatusType.TRUE.getCode());
        Integer firthCount = userService.count(condition1);
        result.put("firthCount", firthCount);
        // 二度人脉统计
        User condition2 = new User();
        condition2.setSecondReferrer(token.getId());
        condition2.setStatus(StatusType.TRUE.getCode());
        Integer secondCount = userService.count(condition2);
        result.put("secondCount", secondCount);
        // 三度人脉统计
        User condition3 = new User();
        condition3.setThirdReferrer(token.getId());
        condition3.setStatus(StatusType.TRUE.getCode());
        Integer thirdCount = userService.count(condition3);
        result.put("thirdCount", thirdCount);
        // 获取推荐人信息
        if (StringUtils.isNotBlank(user.getFirstReferrer())) {
            User referrer = userService.getById(user.getFirstReferrer());
            if (referrer != null) {
                result.put("uid", referrer.getUid());
                result.put("nickName", referrer.getNickName());
                result.put("headImgUrl", referrer.getHeadImgUrl());
                result.put("phone", referrer.getPhone());
            }
        }
        return new JsonResult(result);
    }

    /**
     * 生成分享二维码
     */
    @ResponseBody
    @RequestMapping(value = "/share/qrcode", method = RequestMethod.GET)
    public JsonResult getPicBase64Code(HttpServletRequest request, Integer uid, String groupType) throws Exception {
        if (uid == null) {
            return new JsonResult(0, "邀请码不能为空");
        }
        User condition = new User();
        condition.setUid(uid);
        User referrer = userService.getByCondition(condition);
        if (referrer == null) {
            return new JsonResult(0, "不合法的邀请码");
        }
        if (StringUtils.isEmpty(groupType)) {
            groupType = getGroupType();
        }
        String content = wechatCallbackDomain + "/user/share?uid=" + uid + "&groupType=" + groupType;
        String imgPath = request.getSession().getServletContext().getRealPath("/resources/") + "/css/img/logo.png";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        QRCodeUtil.encode(content, imgPath, baos);
        String rstr = "data:image/jpg;base64," + Base64.encodeToString(baos.toByteArray(), false);
        baos.close();
        return new JsonResult(rstr);
    }

    /**
     * 微信版本生成分享二维码
     */
    @ResponseBody
    @RequestMapping(value = "/wxstore/qrcode", method = RequestMethod.GET)
    public JsonResult getWeiXinStoreBase64QrCode(HttpServletRequest request, Integer uid, String groupType, String storeId) throws Exception {
        if (uid == null) {
            return new JsonResult(0, "邀请码不能为空");
        }
        if (ToolUtil.isEmpty(storeId)) {
            return new JsonResult(0, "店铺信息不能为空");
        }
        Store store = storeService.getById(storeId);
        if (store == null) {
            return new JsonResult(0, "获取店铺信息失败");
        }

        User condition = new User();
        condition.setUid(uid);
        User referrer = userService.getByCondition(condition);
        if (referrer == null) {
            return new JsonResult(0, "不合法的邀请码");
        }
        User condition1 = new User();
        condition1.setStoreId(storeId);
        User storeUser = userService.getByCondition(condition1);
        if (storeUser == null) {
            return new JsonResult(0, "本店铺账户已被禁用");

        }
        if (StringUtils.isEmpty(groupType)) {
            groupType = getGroupType();
        }
//        String content = wechatCallbackDomain + "/user/share?uid=" + uid + "&groupType=" + groupType + "&storeId=" + storeId + "&storeUserUid=" + storeUser.getUid();
        //微信店铺邀请有惊喜url
        String content = "http://doupaimall.com/wxstore" + "/user/wdtransition?uid=" + uid + "&groupType=" + groupType + "&storeId=" + storeId + "&storeUserUid=" + storeUser.getUid() + "&shareUrl=http://doupaimall.com/wxpage" + "_" + "&index=shareCode";
        String imgPath = store.getShareUrl();
        if (ToolUtil.isEmpty(imgPath)) {
            return new JsonResult(0, "本店铺尚未完善信息");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        QRCodeUtil.encode(content, imgPath, baos);
        String rstr = "data:image/jpg;base64," + Base64.encodeToString(baos.toByteArray(), false);
        baos.close();
        return new JsonResult(rstr);
    }

    /**
     * 微信分享(兼容微信店铺版)
     */
    @RequestMapping(value = "/share", method = RequestMethod.GET)
    public ModelAndView share(HttpServletRequest request, HttpServletResponse response, Integer uid, String groupType, String storeId) throws Exception {
        int flag = 0;
        ModelAndView mv = new ModelAndView();
        Integer storeUserUid = 0;
        if (uid == null && ToolUtil.isEmpty(storeId)) {
            mv.setViewName("error");
            mv.addObject("msg", "邀请码不能为空");
            return mv;
        }
        if (ToolUtil.isNotEmpty(storeId)) {
            Store store = storeService.getById(storeId);
            if (store == null) {
                mv.addObject("msg", "该企业尚未开通店铺");
                return mv;
            }
            if (store.getWeixinStatus() == null || store.getWeixinStatus() != 2) {
                return new ModelAndView(new RedirectView("http://doupaimall.com/wxpage/?index=errorPage"));
            }
            User condition1 = new User();
            condition1.setStoreId(storeId);
            User storeUser = userService.getByCondition(condition1);
            if (storeUser == null) {
                mv.addObject("msg", "店铺账户已被禁用");
                return mv;
            }
            storeUserUid = storeUser.getUid();
            if (uid != null) {
                flag = 1;//有邀请码
            } else {
                uid = storeUserUid;
                flag = 2;//无邀请码 以企业用户uid为邀请码
            }
        }
        if (uid == null || uid == 0) {
            mv.setViewName("error");
            mv.addObject("msg", "邀请码不能为空");
            return mv;
        }
        User condition = new User();
        condition.setUid(uid);
        User referrer = userService.getByCondition(condition);
        if (referrer == null) {
            mv.setViewName("error");
            mv.addObject("msg", "不合法的邀请码");
            return mv;
        }
        if (BrowserUtil.isWechat(request)) { // 进入微信授权页
            String redirect_uri = wechatCallbackDomain + "/user/share/callback";
            redirect_uri = URLEncoder.encode(redirect_uri, "utf8");
            //state组装uid+grouptype，用符号"_"分割
            String authorize = "";
            authorize = WechatApiUtil.authorize(redirect_uri, WechatCommonUtil.BASE_SCOPE, uid + "_" + groupType + "_" + flag + "_" + storeId);
            if (StringUtils.isNoneBlank(authorize)) {
                response.sendRedirect(authorize);
                return null;
            }
        }
        // 进入下载页
//        SysSetting setting = sysSettingService.get();
//        mv.setViewName("download");
//        mv.addObject("uid", uid);
//        mv.addObject("androidAppUrl", setting.getAndroidAppUrl());
//        mv.addObject("iosAppUrl", setting.getIosAppUrl());
//        return mv;
        return null;
    }

    /**
     * 微信分享回调(兼容微信店铺版)
     */
    @RequestMapping(value = "/share/callback", method = RequestMethod.GET)
    public ModelAndView share(HttpServletRequest request, HttpServletResponse response, String code, String state) throws Exception {
        ModelAndView mv = new ModelAndView();
        JSONObject json = WechatApiUtil.accessToken(code, state);
        logger.error("*************************************************");
        logger.error("*************************************************");
        logger.error("授权后 json数据 =====> " + json);
        logger.error("*************************************************");
        logger.error("*************************************************");
        if (json == null || !json.has("subscribe")) {
            mv.setViewName("error");
            mv.addObject("msg", "获取您的公开信息（昵称、头像等）失败");
            return mv;
        }
        String openid = WechatApiUtil.getJsonByKey(json, "unionid");
        String nickName = "";
        String headImgUrl = "";
        if (json.getInt("subscribe") == 1) {
            nickName = WechatApiUtil.getJsonByKey(json, "nickname");
            nickName = nickName.replaceAll("[^\u0000-\uFFFF]", "?"); // 过滤非UTF-8字符集,用"?"代替，如Emoji表情
            headImgUrl = WechatApiUtil.getJsonByKey(json, "headimgurl");
        }


        User user = new User();
        user.setWeixin(openid);
        user = userService.getByCondition(user);
        if (null == user) {
            user = new User();
            user.setWeixin(openid);
            nickName = EmojiUtil.replaceEmoji(nickName);
            user.setNickName(nickName);
            user.setHeadImgUrl(headImgUrl);
            if (ToolUtil.isEmpty(headImgUrl)) {
                user.setHeadImgUrl("http://user.doupaimall.com/userDefault.png");
            }
            userService.add(user);
        }
        //根据符号"_"分割出uid和grouptype
        String groupType = "";
        String refferUid = "";
        String flagStr = "";
        String storeId = "";
        String[] strSplit = state.split("_");
        if (strSplit.length > 1) {
            refferUid = strSplit[0];
            groupType = strSplit[1];
            flagStr = strSplit[2];
            storeId = strSplit[3];
            if (groupType == null || ("null").equals(groupType) || groupType.equals("undefined") || StringUtils.isEmpty(groupType)) {
                groupType = getGroupType();
            }
        } else {
            refferUid = strSplit[0];
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
            temp.setLoginTime(new Date());
            temp.setUpdateTime(new Date());
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
        if (ToolUtil.isNotEmpty(flagStr) && ToolUtil.isNotEmpty(storeId)) {
            // 登录
            String token = TokenUtil.generateToken(user.getId(), user.getUserName(), user.getNickName());
            Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
            Sets.sadd(RedisKey.ALL_TOKENS.getKey(), token);
            if (logger.isInfoEnabled()) {
                logger.info(String.format("user login[%s]", TokenUtil.getTokenObject(token)));
            }
            Store store = storeService.getById(storeId);
            if (store == null || store.getWeixinStatus() == null || store.getWeixinStatus() != 2) {
                return new ModelAndView(new RedirectView("http://doupaimall.com/wxpage/?index=errorPage"));
            }
            if (ToolUtil.isNotEmpty(store.getShareUrl())) {
//                mv.setViewName("redirect:" + store.getQrcodeUrl()+"?token="+token+"&storeId="+storeId);
                return new ModelAndView(new RedirectView(store.getShareUrl() + "?token=" + token + "&storeId=" + storeId));
            } else {
                /*mv.setViewName("error");
                mv.addObject("msg", "该企业尚未完善公众号信息");
                return mv;*/
                return new ModelAndView(new RedirectView("http://doupaimall.com/wxpage?index=errorPage"));
            }
        } else {
            // 进入下载页
            mv.setViewName("redirect:" + apiDomain + "/user/download?uid=" + refferUid);
        }
        return mv;
    }

    /**
     * App下载页面
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ModelAndView share(HttpServletRequest request, String uid) throws Exception {
        ModelAndView mv = new ModelAndView();
        // 进入下载页
        mv.setViewName("download");
        mv.addObject("uid", uid);
        mv.addObject("androidAppUrl", ToolUtil.parseString(ParamUtil.getIstance().get(Parameter.ANDROIDAPPURL), "javascript:void(0);"));
        mv.addObject("iosAppUrl", ToolUtil.parseString(ParamUtil.getIstance().get(Parameter.IOSAPPURL), "javascript:void(0);"));
        return mv;
    }

    /**
     * 手机号登录
     */
    @ResponseBody
    @RequestMapping(value = "/login/loginIn", method = RequestMethod.POST)
    public JsonResult loginByUserName(HttpServletRequest request, @RequestBody UserVo vo) throws Exception {
        if (org.springframework.util.StringUtils.isEmpty(vo.getKey())) {
            return new JsonResult(-1, "key不能为空");
        }
        String picCode2 = Strings.get(RedisKey.FORGET_LOGIN_PIC_CODE.getKey() + vo.getKey());
        if (org.springframework.util.StringUtils.isEmpty(picCode2) || !vo.getPicCode().equalsIgnoreCase(picCode2)) {
            return new JsonResult(-1, "图片验证码不正确或已过期");
        }
        int smsCode = (int) ((Math.random() * 9 + 1) * 100000);
        SmsUtil.sendSmsCode(vo.getPhone(), SmsTemplate.BIND_PHONE, smsCode + "");
        Strings.setEx(RedisKey.SMS_CODE.getKey() + vo.getPhone(), RedisKey.SMS_CODE.getSeconds(), smsCode + "");
        Strings.del(vo.getKey());
        User condition = new User();
        if (org.springframework.util.StringUtils.isEmpty(vo.getLoginName())) {
            return new JsonResult(-1, "登录用户名不能为空");
        }
        if (vo.getLoginName().length() < 11) {
            condition.setUid(Integer.valueOf(vo.getLoginName()));
        } else {
            condition.setPhone(vo.getLoginName());
        }
        User user = userService.getByCondition(condition);
        if (null == user) {
            return new JsonResult(-1, "用户不存在");
        } else {
            if (user.getStatus() == StatusType.FALSE.getCode()) {
                return new JsonResult(10002, "您的帐号已被禁用");
            }
            String password = user.getPassword();
            if (org.springframework.util.StringUtils.isEmpty(password)) {
                return new JsonResult(-1, "没有设置登录密码");
            }
            if (!password.equals(Md5Util.MD5Encode(vo.getPassword(), user.getSalt()))) {
                return new JsonResult(-1, "用户名或密码错误");
            }
        }
        // 每次登录都会保存极光推送生成ID
        if (StringUtils.isNoneBlank(vo.getRegistrationId()) && !vo.getRegistrationId().equals(user.getRegistrationId())) {
            userService.updateRegistrationId(user.getId(), vo.getRegistrationId());
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setLoginTime(new Date());
        updateUser.setUpdateTime(new Date());
        userService.update(user.getId(), updateUser);
        // 登录
        String token = TokenUtil.generateToken(user.getId(), user.getUserName(), user.getNickName());
        loginRedis(user, token);
//		Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
        Sets.sadd(RedisKey.ALL_TOKENS.getKey(), token);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("user login[%s]", TokenUtil.getTokenObject(token)));
        }
        UserVo u = new UserVo();
        BeanUtils.copyProperties(u, user);
        u.setToken(token);
        if (ToolUtil.isEmpty(user.getOldUnionId())) {
            u.setIsBindSHWeiXin(StatusType.TRUE.getCode());
        } else {
            u.setIsBindSHWeiXin(StatusType.FALSE.getCode());
        }
        if (ToolUtil.isEmpty(user.getAppOpenId())) {
            u.setIsBindAppOpenId(StatusType.TRUE.getCode());
        } else {
            u.setIsBindAppOpenId(StatusType.FALSE.getCode());
        }
        u.setScore(FormatUtils.formatDouble(user.getScore()));

        setUserVo(user, u);
        return new JsonResult(u);
    }

    /**
     * 忘记登录密码
     */
    @ResponseBody
    @RequestMapping(value = "/forget/loginPassword", method = RequestMethod.POST)
    public JsonResult forgetLoginPassword(HttpServletRequest request, @RequestBody loginPasswordVo vo) throws Exception {
        if (StringUtils.isBlank(vo.getPhone())) {
            return new JsonResult(0, "请输入手机号");
        }
        if (StringUtils.isBlank(vo.getSmsCode())) {
            return new JsonResult(1, "请输入短信验证码");
        }
        String smsCode2 = Strings.get(RedisKey.SMS_CODE.getKey() + vo.getPhone());
        if (!vo.getSmsCode().equalsIgnoreCase(smsCode2)) {
            return new JsonResult(2, "短信验证码不正确或已失效");
        }
        User condition = new User();
        condition.setPhone(vo.getPhone());
        User user = userService.getByCondition(condition);
        if (user == null) {
            return new JsonResult(-1, "该手机号不存在");
        }
        if (StringUtils.isBlank(vo.getNewLoginPass())) {
            return new JsonResult(1, "请输入新密码");
        }
        if (StringUtils.isBlank(vo.getNewLoginPassConfirm())) {
            return new JsonResult(3, "请输入确认新密码");
        }
        if (!vo.getNewLoginPass().equals(vo.getNewLoginPassConfirm())) {
            return new JsonResult(7, "两次输入密码不一致");
        }
        // 更新用户登录密码
        User model = new User();
        model.setPassword(Md5Util.MD5Encode(vo.getNewLoginPass(), user.getSalt()));
        userService.update(user.getId(), model);
        return new JsonResult();
    }

    /**
     * 忘记支付密码
     */
    @ResponseBody
    @RequestMapping(value = "/forget/payPassword", method = RequestMethod.POST)
    public JsonResult forgetPayPassword(HttpServletRequest request, @RequestBody PayPasswordVo vo) throws Exception {
        if (StringUtils.isBlank(vo.getPhone())) {
            return new JsonResult(0, "请输入手机号");
        }
        if (StringUtils.isBlank(vo.getSmsCode())) {
            return new JsonResult(1, "请输入短信验证码");
        }
        String smsCode2 = Strings.get(RedisKey.SMS_CODE.getKey() + vo.getPhone());
        if (!vo.getSmsCode().equalsIgnoreCase(smsCode2)) {
            return new JsonResult(2, "短信验证码不正确或已失效");
        }
        User condition = new User();
        condition.setPhone(vo.getPhone());
        User user = userService.getByCondition(condition);
        if (user == null) {
            return new JsonResult(-1, "该手机号不存在");
        }
        if (StringUtils.isBlank(vo.getNewPayPass())) {
            return new JsonResult(1, "请输入新密码");
        }
        if (StringUtils.isBlank(vo.getNewPayPassConfirm())) {
            return new JsonResult(3, "请输入确认新密码");
        }
        if (!vo.getNewPayPass().equals(vo.getNewPayPassConfirm())) {
            return new JsonResult(7, "两次输入密码不一致");
        }
        // 更新用户支付密码
        User model = new User();
        model.setPayPwd(Md5Util.MD5Encode(vo.getNewPayPassConfirm(), user.getSalt()));
        userService.update(user.getId(), model);
        return new JsonResult();
    }

    /**
     * 设置/修改登录密码
     */
    @ResponseBody
    @RequestMapping(value = "/change/loginPwd", method = RequestMethod.POST)
    public JsonResult changePayPwd(HttpServletRequest request, @RequestBody LoginPwdVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (StringUtils.isNotBlank(user.getPassword())) {
            if (StringUtils.isBlank(vo.getOldLoginPwd())) {
                return new JsonResult(1, "请输入原登录密码");
            }
            if (!user.getPassword().equals(Md5Util.MD5Encode(vo.getOldLoginPwd(), user.getSalt()))) {
                return new JsonResult(2, "原登录密码错误");
            }
        }
        if (StringUtils.isBlank(vo.getNewLoginPwd())) {
            return new JsonResult(3, "请输入新登录密码");
        }
        if (vo.getOldLoginPwd().equals(vo.getNewLoginPwd())) {
            return new JsonResult(4, "新旧登录密码不能相同");
        }
        if (!vo.getNewLoginPwd().equals(vo.getNewLoginPwdConfirm())) {
            return new JsonResult(5, "两次登录密码不一致");
        }
        boolean b = PasswordUtil.checkPass(vo.getNewLoginPwd());
        if (!b) {
            return new JsonResult(6, "密码不符合规范");
        }
        // 更新用户登录密码
        User model = new User();
        model.setPassword(Md5Util.MD5Encode(vo.getNewLoginPwd(), user.getSalt()));
        userService.update(token.getId(), model);
        return new JsonResult();
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
        if (ToolUtil.isEmpty(user.getOldUnionId())) {
            vo.setIsBindSHWeiXin(StatusType.TRUE.getCode());
        } else {
            vo.setIsBindSHWeiXin(StatusType.FALSE.getCode());
        }
        if (ToolUtil.isEmpty(user.getAppOpenId())) {
            vo.setIsBindAppOpenId(StatusType.TRUE.getCode());
        } else {
            vo.setIsBindAppOpenId(StatusType.FALSE.getCode());
        }
        vo.setToken(redisToken);
        vo.setScore(FormatUtils.formatDouble(user.getScore()));
        setUserVo(user, vo);
        List<UserBankcard> userBankcardList = userBankcardService.getList(token.getId());
        if (ToolUtil.isNotEmpty(userBankcardList)) {
            UserBankcard model = userBankcardList.get(0);
            vo.setUserBankcard(model);
        }

        return new JsonResult(vo);
    }

    /*
     * 查询会员和EP信息
     */
    @ResponseBody
    @RequestMapping(value = "/mypartner/partnerinfo", method = RequestMethod.GET)
    public JsonResult getMypartnerInfo(HttpServletRequest request) throws Exception {
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
        // 已经加入合伙人的，需要先计算会员等级
        gradeService.updatePartnerInfo(token.getId());
        user = userService.getById(token.getId());
        Grade grade = gradeService.getGradeDetil(user.getGrade());
        Double award = 0.0d;
        if (grade != null && grade.getTotalSignNo() != null && user.getRemainSign() >= 0) {
            WalletSign model = new WalletSign();
            model.setDonateUserId(user.getId());
            award = walletSignService.signTotal(model, grade.getTotalSignNo() - user.getRemainSign());
        }
        Map<String, Object> map = new HashMap<>();

        /**
         1.等级为0，返回1，显示加入合伙人按钮
         2.等级大于等于1，剩余签到次数大于0，返回2，    显示正在签到中
         3，等级为1，剩余签到次数等于0，签到时间为空，返回3，   显示快快提升业绩签到领积分吧
         4，等级为1剩余签到次数等于0，签到时间不为空，返回4，     显示续费
         **/


        if (user.getGrade() == 0) {
            map.put("btnstate", 1);
//			map.put("gradeRemark", GradeType.grade1.getMsg());
        }
        if (user.getGrade() >= 1 && user.getRemainSign() > 0) {
            map.put("btnstate", 2);
//			map.put("gradeRemark", "签到中");
        }
        if (user.getGrade() == 1 && user.getRemainSign() == 0 && user.getSignTime() == null) {
            map.put("btnstate", 3);
//			map.put("gradeRemark", "已经续费，但业绩不足");
        }
        Integer newGrade = user.getGrade();
        if (user.getRemainSign() == 0 && user.getSignTime() != null) {
            map.put("btnstate", 4);
            newGrade = GradeType.grade2.getCode();
//			map.put("gradeRemark","需要续费");
        }
        map.put("gradeRemark", GradeType.fromCode(user.getGrade()).getMsg());
        map.put("consumeEP", user.getConsumeEP());
        map.put("grade", newGrade);
        map.put("groupChildCountA", user.getGroupChildCountA());
        map.put("groupChildCountB", user.getGroupChildCountB());
        map.put("groupChildCountC", user.getGroupChildCountC());
        map.put("performanceOne", user.getPerformanceOne());
        map.put("performanceTwo", user.getPerformanceTwo());
        map.put("performanceThree", user.getPerformanceThree());
        map.put("remainSign", user.getRemainSign());
        map.put("award", award);
        //判断是否已签到过
        boolean isSignIn = false;
        if (user.getSignTime() != null) {
            Integer d1 = DateTimeUtil.getYearMonthDay(Calendar.getInstance());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(user.getSignTime());
            Integer d2 = DateTimeUtil.getYearMonthDay(calendar);
            isSignIn = d1.intValue() == d2.intValue();
        }
        map.put("isSignIn", isSignIn);
        return new JsonResult(map);
    }

    /*
     * 加入合伙人或续费
     */
    @ResponseBody
    @RequestMapping(value = "/mypartner/join", method = RequestMethod.POST)
    public JsonResult joinPartner(HttpServletRequest request) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        Boolean renew = false;
        JsonResult jsonResult = new JsonResult();
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
        if (user.getGrade() != null && user.getGrade() == 0) {
            if (user.getExchangeEP() < 800) {
                return new JsonResult(4, "您的EP不足，暂时无法加入合伙人");
            } else {
                return userService.join(jsonResult, user, 800d);
            }
        }
       /* if (user.getGrade() != null && user.getGrade() > 0) {
            renew = gradeService.renewGrade(token.getId());
            if (renew) {
                jsonResult.setMsg("续费成功");
            } else {
                jsonResult.setMsg("续费失败，详情请查看消息");
            }
        }*/

        return jsonResult;
    }

    /*
     * 用户签到
     */
    @ResponseBody
    @RequestMapping(value = "/signIn", method = RequestMethod.GET)
    public JsonResult signIn(HttpServletRequest request) throws Exception {
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
        // 已经加入合伙人的，需要先计算会员等级
        gradeService.updatePartnerInfo(token.getId());
        user = userService.getById(token.getId());
        if (user.getGrade() == null || user.getGrade() <= 1) {
            logger.debug("用户：" + user.getUserName() + "(" + user.getUid() + ")未达到领取积分的级别");
            return new JsonResult(4, "您的等级未达到领取积分的级别");
        }
        //今天是否已签到过
        if (user.getSignTime() != null) {
            Integer d1 = DateTimeUtil.getYearMonthDay(Calendar.getInstance());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(user.getSignTime());
            Integer d2 = DateTimeUtil.getYearMonthDay(calendar);
            if (d1.intValue() == d2.intValue()) {
                logger.debug("用户：" + user.getUserName() + "(" + user.getUid() + ")今天已签到过，不需重复签到");
                return new JsonResult(7, "已签到过，请勿重复签到");
            }
        }
        if (user.getRemainSign() <= 0) {
            logger.debug("用户：" + user.getUserName() + "(" + user.getUid() + ")剩余签到次数小于等于0，不需要签到。");
            return new JsonResult(8, "会员已到期，不需要签到");
        }
        //签到领取的日薪需要扣除对应的用户持有的斗斗数量
        Integer intGrade = user.getGrade();
        Grade grade = gradeService.getGradeDetil(intGrade);
        if (user.getDoudou() < grade.getDaily() || user.getDoudou() <= 0) {
            logger.debug("用户：" + user.getUserName() + "(" + user.getUid() + ")持有斗斗数量（" + user.getDoudou() + "）不足,今天可领取金额为：" + grade.getDaily());
            return new JsonResult(5, "您现在持有斗斗数量（" + user.getDoudou() + "）不足,今天可领取金额为：" + grade.getDaily() + ",请尽快补充斗斗数量!");
        }
        // 会员签到
        Double signEp = gradeService.memberSignIn(user);
        if (signEp > 0) {
            Map<String, String> map = new HashedMap();
            map.put("signCount", (grade.getTotalSignNo() - user.getRemainSign()) + "");
            map.put("signEP", signEp + "");
            return new JsonResult(map);
        }
        return new JsonResult();
    }


    public static String getGroupType() {
        return group.get(RandomUtil.randomInt(0, 3));
    }


    /**
     * 完善资料
     */
    @ResponseBody
    @RequestMapping(value = "/completeinfo", method = RequestMethod.POST)
    public JsonResult completeInfo(HttpServletRequest request, @RequestBody UserV42Vo vo) throws Exception {
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
        JsonResult x = checkCompleteInfo(user, vo);
        if (x != null) return x;

        User updateUser = new User();
        if (ToolUtil.isNotEmpty(vo.getPassword())) {
            updateUser.setPassword(Md5Util.MD5Encode(vo.getPassword(), user.getSalt()));
        }
        if (ToolUtil.isNotEmpty(vo.getPayPwd())) {
            updateUser.setPayPwd(Md5Util.MD5Encode(vo.getPayPwd(), user.getSalt()));
        }
        if (ToolUtil.isNotEmpty(vo.getPhone())) {
            updateUser.setPhone(vo.getPhone());
        }
        if (ToolUtil.isNotEmpty(vo.getAreaId())) {
            updateUser.setAreaId(vo.getAreaId());
            updateUser.setProvince(vo.getProvince());
            updateUser.setCity(vo.getCity());
            updateUser.setCounty(vo.getCounty());
        }
        updateUser.setId(user.getId());
        try {
            userService.completeInfo(updateUser);
            return new JsonResult(ResultCode.SUCCESS.getCode(), "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(ResultCode.ERROR.getCode(), "保存失败");
        }
    }

    private JsonResult checkCompleteInfo(User user, UserV42Vo vo) {

        if (ToolUtil.isEmpty(user.getPhone()) && ToolUtil.isEmpty(vo.getPhone())) {
            return new JsonResult(4, "手机号不能为空");
        }
        if (ToolUtil.isEmpty(user.getPhone()) && ToolUtil.isNotEmpty(vo.getPhone()) && ToolUtil.isEmpty(vo.getSmsCode())) {
            return new JsonResult(6, "验证码不能为空");
        }
        String checkCode = Strings.get(RedisKey.SMS_CODE.getKey() + vo.getPhone());
        if (ToolUtil.isEmpty(checkCode)) {
            return new JsonResult(6, "验证码已失效");
        }
        if (!checkCode.equals(vo.getSmsCode())) {
            Strings.del(RedisKey.SMS_CODE.getKey() + vo.getPhone());
            return new JsonResult(6, "验证码错误,请重新获取");
        }
        if (ToolUtil.isEmpty(user.getPayPwd()) && ToolUtil.isEmpty(vo.getPayPwd())) {
            return new JsonResult(7, "支付密码不能为空");
        }
        if (ToolUtil.isEmpty(user.getPassword()) && ToolUtil.isEmpty(vo.getPassword())) {
            return new JsonResult(8, "登录密码不能为空");
        }
        if (ToolUtil.isEmpty(user.getAreaId()) && ToolUtil.isEmpty(vo.getAreaId())) {
            return new JsonResult(9, "地址信息不能为空");
        }
        if (ToolUtil.isEmpty(user.getProvince()) && ToolUtil.isEmpty(vo.getProvince())) {
            return new JsonResult(10, "地址信息不能为空");
        }
        if (ToolUtil.isEmpty(user.getCity()) && ToolUtil.isEmpty(vo.getCity())) {
            return new JsonResult(11, "地址信息不能为空");
        }
        if (ToolUtil.isEmpty(user.getCounty()) && ToolUtil.isEmpty(vo.getCounty())) {
            return new JsonResult(12, "地址信息不能为空");
        }
        return null;
    }

    /**
     * 修改昵称
     **/
    @ResponseBody
    @RequestMapping(value = "/updatenick", method = RequestMethod.POST)
    public JsonResult updateNickName(HttpServletRequest request, @RequestBody UserVo vo) throws Exception {
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
        if (ToolUtil.isEmpty(vo.getNickName())) {
            return new JsonResult(4, "昵称不能为空");
        }
        User updateUser = new User();
        updateUser.setNickName(vo.getNickName());
        userService.update(user.getId(), updateUser);
        return new JsonResult(ResultCode.SUCCESS.getCode(), "修改昵称成功");
    }

    /**
     * 修改头像
     **/
    @ResponseBody
    @RequestMapping(value = "/updateheadimg", method = RequestMethod.POST)
    public JsonResult updateHeadimg(HttpServletRequest request, @RequestBody UserVo vo) throws Exception {
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
        if (ToolUtil.isEmpty(vo.getHeadImgUrl())) {
            return new JsonResult(4, "头像不能为空");
        }
        User updateUser = new User();
        updateUser.setHeadImgUrl(vo.getHeadImgUrl());
        userService.update(user.getId(), updateUser);
        return new JsonResult(ResultCode.SUCCESS.getCode(), "修改头像成功");
    }

    public static void main(String[] args) {
        String salt = DateTimeUtil.formatDate(new Date(), DateTimeUtil.PATTERN_B);
        String pass = Md5Util.MD5Encode("123456", salt);
        System.out.println(pass);
        System.out.println(salt);
        System.out.println(pass.equals(Md5Util.MD5Encode("123456", salt)));
        System.out.println("127C5073979AF8DB92E7626E5888EF49".equals(Md5Util.MD5Encode("", salt)));

    }

}
