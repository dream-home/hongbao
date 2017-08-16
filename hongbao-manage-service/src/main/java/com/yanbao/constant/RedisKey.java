package com.yanbao.constant;

/**
 * redis相关key
 * 
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum RedisKey {

	/** test */
	USER_TEST("user_test_",1*60*60),
	/**
	 * 系统商品待发货
	 */
	SYSTEM_EXPRESS_GOODS("system_express_goods_",20),

	/**系统参数更新标致(保存72小时)*/
	SYS_PARAM_UPDTOKEN("sys_param_updtoken", 72*60*60),
	
	/**地区前缀*/
	STORE_CODE_PREFIX("store_code_prefix_", 72*60*60);

	private final String key;

	private final int seconds;

	private RedisKey(String key, int seconds) {
		this.key = key;
		this.seconds = seconds;
	}

	public String getKey() {
		return key;
	}

	public int getSeconds() {
		return seconds;
	}
}
