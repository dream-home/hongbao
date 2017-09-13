package com.yanbao.util;

import com.yanbao.redis.Strings;
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
     * 静默授权
     */
    public static final String BASE_SCOPE = "snsapi_userinfo";

    /**
     * 用户点击授权
     */
    public static final String USERINFO_SCOPE = "snsapi_base";

    /**
     * 深圳斗拍公众号
     */

//    private static final String APPID = WechatCommonUtil.SZDP_PUBLIC_APPID;
//    private static final String SECRET = WechatCommonUtil.SZDP_PUBLIC_SECRET;
    private static String APPID;
    private static String SECRET;

    @Value("${publicAppid}")
    private String publicAppid;
    @Value("${publicSecret}")
    private String publicSecret;

    @PostConstruct
    public void init() {
        APPID = publicAppid;
        SECRET = publicSecret;
    }

    /**
     * 珠海炎宝公众号
     */

//    private static final String APPID = WechatCommonUtil.YANBAO_PUBLIC_APPID;
//    private static final String SECRET = WechatCommonUtil.YANBAO_PUBLIC_SECRET;


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
     * 接口调用access_token接口
     */
    private static final String ACCESS_TOKEN_URL_FOR_API = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    /**
     * 分享调用凭证 jsapi_ticket
     */
    private static final String JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    /**
     * 根据openid获取用户信息接口
     */
    private static final String USERINFO_BY_OPENID_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";


    /**
     * 炎宝公众号授权
     */
    public static String authorizeH5(String redirect_uri, String scope, String state) {
        if (StringUtils.isBlank(scope)) {
            scope = "snsapi_userinfo";
        }
        if (StringUtils.isBlank(redirect_uri)) {
            return null;
        }
        return AUTHORIZE_URL.replace("APPID", APPID).replace("REDIRECT_URI", redirect_uri).replace("SCOPE", scope).replace("STATE", state);
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

    /**
     * 获取分享调用凭证
     *
     * @return
     */
    public static String getJsApiTicket(String accessToken) {
        String ticket = "";
        //缓存ticket
        //从redis中取值
        ticket = Strings.get("ticket_time");
        logger.error("##############################");
        logger.error("从redis中获取ticket ==》 " + ticket);
        logger.error("##############################");
        if (!StringUtils.isEmpty(ticket)) {
            return ticket;
        }
        String requestUrl = JSAPI_TICKET.replace("ACCESS_TOKEN", accessToken);
        JSONObject json = HttpsUtil.doGetRequest(requestUrl);
        logger.error("jsonobj ===> " + json.toString());
        logger.error("**********************");
        if (!json.isNullObject()) {
            try {
                ticket = json.getString("ticket");
                Strings.setEx("ticket_time", 7000, ticket);
                return ticket;
            } catch (Exception e) {
                logger.error(json.toString());
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 通过授权直接获取用户信息（炎宝炎宝公众号）
     *
     * @param code
     * @param state
     * @return
     */
    public static JSONObject accessTokenH5(String code, String state) {
        if (StringUtils.isBlank(code) || "authdeny".equals(code)) {
            return null;
        }
        JSONObject result = null;
        String requestUrl = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("SECRET", SECRET).replace("CODE", code);
        JSONObject json = HttpsUtil.doGetRequest(requestUrl);
        if (!json.isEmpty() && !json.has("errcode")) {
//          老版根据授权获取用户基本信息，新版改为根据openid获取用户信息
            result = getUserInfoByOpenId(getJsonByKey(json, "openid"));
        }
        return result;
    }

    public static JSONObject userInfo(String openid, String access_token) {
        if (StringUtils.isBlank(openid) || StringUtils.isBlank(access_token)) {
            return null;
        }
        String requestUrl = USERINFO_URL.replace("OPENID", openid).replace("ACCESS_TOKEN", access_token);
        return HttpsUtil.doGetRequest(requestUrl);
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
        System.out.println(accessToken);
        String requestUrl = USERINFO_BY_OPENID_URL.replace("OPENID", openid).replace("ACCESS_TOKEN", accessToken);
        System.out.println(requestUrl);
        return HttpsUtil.doGetRequest(requestUrl);
    }

    public static String getJsonByKey(JSONObject jsonObj, String key) {
        String result = "";
        if (jsonObj.get(key) != null) {
            result = jsonObj.getString(key);
        }
        return result;
    }

    public static void main(String[] args) {
        JSONObject object = getUserInfoByOpenId("ouH4bs_JG0XvtFfvLQZUDnWHPCl0");
        System.out.println(object.toString());
    }

}
