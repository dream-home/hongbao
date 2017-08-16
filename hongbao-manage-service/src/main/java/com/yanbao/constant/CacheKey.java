package com.yanbao.constant;

/**
 * Created by summer on 2016-12-12:10:20;
 */
public class CacheKey {

    final static String tokenKey="TOKEN_KEY";

    final static String picCodeKey="PIC_CODE_KEY";

    public static String getTokenKey(String userId){
        return tokenKey+":"+userId;
    }

    public static String getPicCodeKey(String nickName) {
        return picCodeKey+":"+nickName;
    }
}
