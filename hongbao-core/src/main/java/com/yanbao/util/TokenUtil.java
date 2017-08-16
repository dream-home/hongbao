package com.yanbao.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yanbao.core.model.Token;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author zhuzh
 * @date 2016年12月8日
 */
public class TokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    /**
     * 生成token
     *
     * @return
     */
    public static String generateToken(String id, String userName, String nickName) {
        String rs = "";
        try {
            Token token = new Token();
            token.setId(id);
            token.setUserName(userName);
            token.setNickName(nickName);
            token.setTime(new Date().getTime());
            token.setLoginType(0);
            rs = new sun.misc.BASE64Encoder().encode(JSONObject.toJSON(token).toString().getBytes("GBK")).replaceAll("[\\t\\n\\r]", "");;
        } catch (UnsupportedEncodingException e) {
            logger.info("转换json格式错误：" + e.getMessage());
            return "";
        }
        return rs;
    }

    /**
     * 生成token
     *
     * @return
     */
    public static String generateToken(String id, String userName, String nickName, Integer loginType) {
        String rs = "";
        try {
            Token token = new Token();
            token.setId(id);
            token.setUserName(userName);
            token.setNickName(nickName);
            token.setTime(new Date().getTime());
            token.setLoginType(loginType);
            rs = new sun.misc.BASE64Encoder().encode(JSONObject.toJSON(token).toString().getBytes("GBK")).replaceAll("[\\t\\n\\r]", "");;
        } catch (UnsupportedEncodingException e) {
            logger.info("转换json格式错误：" + e.getMessage());
            return "";
        }
        return rs;
//		return Base64.encode(JSON.toJSON(token).toString().getBytes());
    }

    /**
     * 获取Token对象
     *
     * @return
     */
    @SuppressWarnings("static-access")
    public static Object getTokenObject(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(token);
            token = new String(b, "GBK");
            return jsonObject.parseObject(new String(token), Token.class);
        } catch (Exception e) {
            System.out.println("Illegal token[" + token + "]," + e.getMessage());
            return null;
        }
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    public static Token getSessionUser(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (null == token) {
            token = request.getParameter("token");
        }
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return (Token) getTokenObject(token);
    }


    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (null == token) {
            token = request.getParameter("token");
        }
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return token;
    }

    public static void main(String[] args) {
        String token="eyJ0aW1lIjoxNTAxNTc3NTU2NzQyLCJpZCI6IkJCMzdFRUVDOThFNzRFNzVCQTA3NzFFOEU3NDE4RjRFIiwidXNlck5hbWUiOiJoaGgiLCJsb2dpblR5cGUiOjAsIm5pY2tOYW1lIjoiLSJ9";
        Object obj =   getTokenObject(token);
        Token tokenObj = (Token) obj;
        System.out.println(JSON.toJSONString(tokenObj));
    }
}
