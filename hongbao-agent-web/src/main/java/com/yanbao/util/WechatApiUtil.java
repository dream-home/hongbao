package com.yanbao.util;

import com.yanbao.util.HttpsUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author zhuzih
 * @date 2015年11月26日
 */
public class WechatApiUtil {

	private static final String APPID = "wx30135d0dfc5e7350";

	private static final String SECRET = "4671c87286c496f2d9709eb74631c46b";

	/** 用户授权获取code接口 */
	private static final String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

	/** 通过code换取网页授权access_token接口 */
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

	/** 获取用户信息接口(需scope为 snsapi_userinfo) */
	private static final String USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	public static String authorize(String redirect_uri, String scope, String state) {
		if (StringUtils.isBlank(scope)) {
			scope = "snsapi_userinfo";
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
		if (!json.isEmpty() && json.get("errcode") == null) {
			result = userinfo(getJsonByKey(json, "openid"), getJsonByKey(json, "access_token"));
		}
		return result;
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

}
