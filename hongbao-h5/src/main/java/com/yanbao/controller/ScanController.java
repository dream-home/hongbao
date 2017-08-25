package com.yanbao.controller;

import com.yanbao.constant.RedisKey;
import com.yanbao.constant.StatusType;
import com.mall.model.User;
import com.yanbao.redis.Sets;
import com.yanbao.redis.Strings;
import com.yanbao.service.UserService;
import com.yanbao.util.*;
import com.yanbao.vo.ShareVo;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;

/**
 * 商家扫码
 * Created by Administrator on 2017/5/17 0017.
 */
@Controller
@RequestMapping("/scan")
public class ScanController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Value("${wechath5_callback_domain}")
    private String wechatCallbackDomain;
    @Value("${payPage}")
    private String payPage;
    private static final String PAGE_SHARE ="http://doupaimall.com/h5";

    /**
     * 微信分享过渡页
     */
    @RequestMapping(value = "/wxlogin", method = RequestMethod.POST)
    public ModelAndView share(HttpServletRequest request, HttpServletResponse response,ShareVo vo) throws Exception {
        logger.error("*******wxlogin*********");
        ModelAndView mv = new ModelAndView();
        if (vo.getUid() == null) {
            mv.setViewName("error");
            mv.addObject("msg", "邀请码不能为空");
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
        if ( StringUtils.isEmpty(vo.getType()) || vo.getType().equals("null") || vo.getType().equals("undefined")) {
            mv.setViewName("error");
            mv.addObject("msg", "二维码类型错误");
            return mv;
        }
        if (BrowserUtil.isWechat(request)) { // 进入微信授权页
            String redirect_uri = wechatCallbackDomain + "/scan/callback";
            redirect_uri = URLEncoder.encode(redirect_uri, "utf8");
            //state组装uid_index，用符号"_"分割  200828_index
            String authorize = WechatApiUtil.authorizeH5(redirect_uri, WechatApiUtil.BASE_SCOPE, vo.getUid() + "_"+ vo.getIndex()+"_"+vo.getStoreUserId()+"_"+vo.getStoreId()+"_"+vo.getType());
            logger.error("**************************");
            logger.error(authorize);
            logger.error("**************************");
            if (StringUtils.isNoneBlank(authorize)) {
                response.sendRedirect(authorize);
                return null;
            }
        }
        // 进入提示页面
        mv.setViewName("error");
        mv.addObject("msg", "授权失败");
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
            if(ToolUtil.isEmpty(headImgUrl)){
                user.setHeadImgUrl("http://user.doupaimall.com/userDefault.png");
            }
            userService.add(user);
        }
        User tem = new User();
        tem.setOpenId(openid);
        tem.setLoginTime(new Date());
        userService.update(user.getId(), tem);
        //根据符号"_"分割出uid和grouptype
        String refferUid="";
        String index="";
        String storeUserId="";
        String storeId="";
        String type="";
        String[] strSplit = state.split("_");
        if(strSplit.length==5){
            refferUid = strSplit[0];
            index = strSplit[1];
            storeUserId=strSplit[2];
            storeId=strSplit[3];
            type=strSplit[4];

            if (index==null  || ("null").equals(index)  || index.equals("undefined") ||  StringUtils.isEmpty(index) || !"4".equals(index) ) {
                index="4";
            }
        }else {
            logger.error("商家二维码错误99999 ："+state );
            mv.setViewName("error");
            mv.addObject("msg", "微信扫码支付参数错误");
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
            temp.setLevles(referrer.getLevles()+1);
            temp.setOpenId(openid);
            temp.setLoginTime(new Date());
            userService.update(user.getId(), temp);

            // 异步处理分组业务
            final String userId = user.getId();
            final String finalGroupType = "";
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
        mv.addObject("info","进入付款页面");
        String page=payPage + "?uid=" + refferUid+"&token="+token+"&index="+index+"&storeUserId="+storeUserId+"&storeId="+storeId+"&type="+type;
        logger.error("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        logger.error("%%%%%%%%%%%%%%%%%%%%%进入付款页面%%%%%%%%%%%%%%%");
        logger.error("调整路径："+page);
        logger.error("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//        String pa="http://120.76.43.39:8090/h5/app.html";
        mv.setViewName("redirect:" + payPage + "?uid=" + refferUid+"&token="+token+"&index="+index+"&storeUserId="+storeUserId+"&storeId="+storeId+"&type="+type);
//        mv.setViewName("redirect:" + pa + "?uid=" + refferUid+"&token="+token+"&index="+index+"&storeUserId="+storeUserId+"&storeId="+storeId+"&type="+type);
        return mv;
    }
}
