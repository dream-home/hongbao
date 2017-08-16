package com.yanbao.interceptor;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yanbao.constant.CacheKey;
import com.yanbao.constant.ResultCode;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.redis.Strings;
import com.yanbao.util.FileUtil;
import com.yanbao.util.TokenUtil;

/**
 * action拦截类，做登录验证
 */
@Component
public class ActionInterceptor extends HandlerInterceptorAdapter {

	public static List<String> noAuthorized = new ArrayList<String>();

	static {
		Iterator<?> iterator = FileUtil.findXMLForAll("url", "no_authorized.xml");
		if (iterator != null) {
			while (iterator.hasNext()) {
				Element urlElement = (Element) iterator.next();
				String url = urlElement.getText();
				noAuthorized.add(url);
			}
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	/**
	 * 拦截所有spring MVC请求。如果该请求路径是首页或者不需要授权的路径（noAuthorized）则，不受任何影响。
	 * 否则，如果其他路径，且用户未登陆，则返回到首页。
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (request.getMethod().toString().equals("OPTIONS")){
			JSONObject responseJSONObject = JSONObject.fromObject(new JsonResult(200,"成功"));
			PrintWriter out = response.getWriter();
			out.append(responseJSONObject.toString());
			return false;
		}

		String uri = request.getRequestURI();
		if (uri.matches(".+/image/.+")){
			return true;
		}
		// 不在配置列表中的直接排除
		if (noAuthorized.contains(uri)) {
			return true;
		}
		// 在Header中获取Token
		String token = request.getHeader("token");
		if (null == token) {
			token = request.getParameter("token");
		}
		if (StringUtils.isEmpty(token)) {
			JSONObject responseJSONObject = JSONObject.fromObject(new JsonResult(ResultCode.NO_LOGIN.getCode(),"请登录"));
			PrintWriter out = response.getWriter();
			out.append(responseJSONObject.toString());
			return false;
		}
		Object obj = TokenUtil.getTokenObject(token);
		if (obj == null) {
			JSONObject responseJSONObject = JSONObject.fromObject(new JsonResult(ResultCode.NO_LOGIN.getCode(), "请登录"));
			PrintWriter out = response.getWriter();
			out.append(responseJSONObject.toString());
			return false;
		}
		// Redis获取Token
		Token tokenObj = (Token) obj;
		String redisToken = Strings.get(CacheKey.getTokenKey(tokenObj.getId()));
		if (StringUtils.isEmpty(redisToken) || !token.equals(redisToken)) {
			JSONObject responseJSONObject = JSONObject.fromObject(new JsonResult(ResultCode.NO_LOGIN.getCode(),"请登录"));
			PrintWriter out = response.getWriter();
			out.append(responseJSONObject.toString());
			return false;
		}
		// 检验通过，更新redis中token生命周期
		Strings.setEx(CacheKey.getTokenKey(tokenObj.getId()), 30 * 60, token);
		return true;

	}

}
