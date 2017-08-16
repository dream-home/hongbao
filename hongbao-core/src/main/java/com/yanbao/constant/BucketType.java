package com.yanbao.constant;

/**
 * 图片空间枚举类
 *
 * @date 2016年11月15日
 */
public enum BucketType {

    DOUPAI_TEST_USER("doupai-test-user", "斗拍测试用户"),
    DOUPAI_TEST_STORE("doupai-test-store", "斗拍测试商家"),
    DOUPAI_TEST_GOODS("doupai-test-goods", "斗拍测试商品"),
    DOUPAI_TEST_AD("doupai-test-ad", "斗拍测试广告"),
    DOUPAI_TEST_BANNERS("doupai-test-banners", "斗拍测试功能菜单"),
    DOUPAI_OFFICAL_USER("doupai-offical-user", "斗拍正式用户"),
    DOUPAI_OFFICAL_STORE("doupai-offical-sotre", "斗拍正式商家"),
    DOUPAI_OFFICAL_GOODS("doupai-offical-goods", "斗拍正式商品"),
    DOUPAI_OFFICAL_AD("doupai-offical-ad", "斗拍正式广告"),
    DOUPAI_OFFICAL_BANNERS("doupai-offical-banners", "斗拍正式功能菜单");


    private final String code;

    private final String msg;

    private BucketType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
