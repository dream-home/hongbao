package com.yanbao.util;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;

/**
 * 阿里大于短信接口
 */
public class SmsUtil {

	private static final String APPID = "23496041";

	private static final String APPSECRET = "fad4652bb6cd86c040d3aafdf407cf15";

	/**
	 * 发送短信验证码，手机号码多个用“,”隔开.
	 */
	public static void sendSmsCode(String phone, SmsTemplate smsTemplate, String smsCode) throws ApiException {
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", APPID, APPSECRET);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("");
		req.setSmsType("normal");
		req.setSmsFreeSignName("斗拍商城");
		req.setSmsParamString("{'code':'" + smsCode + "'" + smsTemplate.getParam() + "}");
		req.setRecNum(phone);
		req.setSmsTemplateCode(smsTemplate.getCode());
		client.execute(req);
	}

}