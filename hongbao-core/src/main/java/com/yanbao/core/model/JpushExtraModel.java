package com.yanbao.core.model;

/**
 * 极光推送扩展字段
 *
 * @author jay.zheng
 * @date 2017年6月7日
 */
public class JpushExtraModel {
    /***
     * 扩展字段名称：跳转类型（前端接收此参数判断）
     * */
    public static final String NOTIFIYPE = "Notifitype";
    /***
     * 扩展字段内容：前端跳到用户订单
     * */
    public static final String USER_ORDER = "1";
    /***
     * 扩展字段内容：前端跳到商家订单
     * */
    public static final String STORE_ORDER = "2";
    /***
     * 扩展字段内容：前端跳到消费记录消息
     * */
    public static final String EXPENSE_MSM = "3";
    /***
     * 扩展字段内容：前端跳到系统信息
     * */
    public static final String SYSTEM_MSM = "4";
    private String key;//扩展字段名称

    private String value;//扩展字段内容

    //覆盖key,value
    public JpushExtraModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    private void setKey(String key) {
        this.key = key;
    }

    private void setValue(String value) {
        this.value = value;
    }

}
