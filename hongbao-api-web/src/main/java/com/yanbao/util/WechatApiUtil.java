package com.yanbao.util;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author zhuzih
 * @date 2015年11月26日
 */
@Component
public class WechatApiUtil {

    public static Logger logger = LoggerFactory.getLogger(WechatApiUtil.class);
    /**
     * 正式
     */

//    private static final String APPID = WechatCommonUtil.ZHDP_PUBLIC_APPID;
//    private static final String SECRET = WechatCommonUtil.ZHDP_PUBLIC_APPSECRET;

//    private static final String APPID = WechatCommonUtil.SZDP_PUBLIC_APPID;
//    private static final String SECRET = WechatCommonUtil.SZDP_PUBLIC_SECRET;

    private static   String APPID;
    private static   String SECRET ;

    @Value("${appPublicAppid}")
    private String appPublicAppid;
    @Value("${appPublicSecret}")
    private String appPublicSecret;
    @PostConstruct
    public void init(){
        APPID=appPublicAppid;
        SECRET=appPublicSecret;
    }

    /**
     * 用户授权获取code接口
     */
    private static final String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

    /**
     * 通过code换取网页授权access_token接口
     */
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    /**
     * 获取用户信息接口(需scope为 snsapi_userinfo)
     */
    private static final String USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /**
     * 根据openid获取用户信息接口
     */
    private static final String USERINFO_BY_OPENID_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /**
     * 接口调用access_token接口
     */
    private static final String ACCESS_TOKEN_URL_FOR_API = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    public static String authorize(String redirect_uri, String scope, String state) {
        if (StringUtils.isBlank(scope)) {
            scope =WechatCommonUtil.USERINFO_SCOPE;
        }
        if (StringUtils.isBlank(redirect_uri)) {
            return null;
        }
        return AUTHORIZE_URL.replace("APPID", APPID).replace("REDIRECT_URI", redirect_uri).replace("SCOPE", scope).replace("STATE", state);
    }

    public static JSONObject accessToken(String code, String state) {
        if (StringUtils.isBlank(code) || "authdeny".equals(code)) {
            return null;
        }
        JSONObject result = null;
        String requestUrl = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("SECRET", SECRET).replace("CODE", code);
        JSONObject json = HttpsUtil.doGetRequest(requestUrl);
        logger.error("网页授权json " + json);
        if (!json.isEmpty() && !json.has("errcode")) {
//          老版根据授权获取用户基本信息，新版改为根据openid获取用户信息
            if (!json.isEmpty() && !json.has("errcode")) {
                result = getUserInfoByOpenId(getJsonByKey(json, "openid"));
            }
        }
        return result;
    }

    /**
     * 根据用户授权后的openid，以及调用接口的基本凭证accessToken获取用户基本信息
     *
     * @param openid
     * @return
     */
    public static JSONObject getUserInfoByOpenId(String openid) {
        String accessToken = WechatApiUtil.accessTokenForAPi();
        if (StringUtils.isBlank(openid) || StringUtils.isBlank(accessToken)) {
            return null;
        }
        String requestUrl = USERINFO_BY_OPENID_URL.replace("OPENID", openid).replace("ACCESS_TOKEN", accessToken);
        return HttpsUtil.doGetRequest(requestUrl);
    }


    public static JSONObject userinfo(String openid, String access_token) {
        if (StringUtils.isBlank(openid) || StringUtils.isBlank(access_token)) {
            return null;
        }
        String requestUrl = USERINFO_URL.replace("OPENID", openid).replace("ACCESS_TOKEN", access_token);
        return HttpsUtil.doGetRequest(requestUrl);
    }

    public static String getJsonByKey(JSONObject jsonObj, String key) {
        String result = "";
        if (jsonObj.get(key) != null) {
            result = jsonObj.getString(key);
        }
        return result;
    }

    /**
     * 接口调用获取access_token
     *
     * @return
     */
    public static String accessTokenForAPi() {
        String accessToken = null;
        String requestUrl = ACCESS_TOKEN_URL_FOR_API.replace("APPID", APPID).replace("APPSECRET", SECRET);
        JSONObject json = HttpsUtil.doGetRequest(requestUrl);
        logger.error("jsonobj ===> " + json.toString());
        logger.error("**********************");
        if (!json.isNullObject()) {
            try {
                accessToken = json.getString("access_token");
                return accessToken;
            } catch (Exception e) {
                logger.error(json.toString());
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        JSONObject object = getUserInfoByOpenId("oVuyGwCtIws54x5aBUNuBm1MWn7s");
        System.out.println(object.toString());
    }
}
