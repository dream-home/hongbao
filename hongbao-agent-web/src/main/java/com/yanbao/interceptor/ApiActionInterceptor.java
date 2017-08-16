package com.yanbao.interceptor;

import com.yanbao.constant.RedisKey;
import com.yanbao.constant.ResultCode;
import com.yanbao.core.exception.CommonException;
import com.yanbao.core.model.Token;
import com.yanbao.redis.Sets;
import com.yanbao.redis.SortSet;
import com.yanbao.redis.Strings;
import com.yanbao.util.FileUtil;
import com.yanbao.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * action拦截类，做登录验证
 */
@Component
public class ApiActionInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ApiActionInterceptor.class);

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

		String uri = request.getRequestURI();
		// 过滤配置列表中的URL
		for (String uriKey : noAuthorized) {
			if (uri.indexOf(uriKey) > 0){
				return true;
			}
		}
		// 在Header中获取Token
		String token = request.getHeader("token");
		if (null == token) {
			token = request.getParameter("token");
		}
		System.err.println("token:"+token);
		if (StringUtils.isEmpty(token)) {
			throw new CommonException(ResultCode.NO_TOKEN.getCode(), ResultCode.NO_TOKEN.getMsg());
		}

		logger.error("******************token*********************");
		logger.error(token);
		logger.error("********************************************");
		Object obj = TokenUtil.getTokenObject(token);
		if (obj == null) {
			throw new CommonException(ResultCode.NO_LOGIN.getCode(), ResultCode.NO_LOGIN.getMsg());
		}
		// Redis获取Token
		Token tokenObj = (Token) obj;
		String redisToken = Strings.get(RedisKey.TOKEN_API.getKey() + tokenObj.getId());

		if (StringUtils.isEmpty(redisToken) || !token.equals(redisToken)) {
			Long index = SortSet.zrank(RedisKey.TOKEN_API_STORE.getKey()+tokenObj.getId(),token);
			if (index==null){
				throw new CommonException(ResultCode.NO_LOGIN.getCode(), ResultCode.NO_LOGIN.getMsg());
			}
			Sets.expireSetSecond(RedisKey.TOKEN_API_STORE.getKey()+tokenObj.getId(), RedisKey.TOKEN_API_STORE.getSeconds());
		}
		// 检验通过，更新redis中token生命周期
		Strings.setEx(RedisKey.TOKEN_API.getKey() + tokenObj.getId(), RedisKey.TOKEN_API.getSeconds(), token);
		return true;
	}
}
