package com.yanbao.constant;

import com.yanbao.util.PoundageUtil;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * redis相关key
 * 
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum RedisKey {
	/**all id，保存2小时*/
	WAITING_IDS("waiting_ids", 2 * 60 * 60),
	/**all id，保存2小时 */
	ALL_IDS("all_ids", 2 * 60 * 60),
	/**all test Token，保存2小时 */
	ALL_TEST_TOKENS("all_test_tokens", 20),
	/**all Token，保存1天 */
	ALL_TOKENS("all_tokens", 1 * 60),
	/**all un_read_message，保存1天 */
	UN_READ_MESSAGE("un_read_message", 24 * 60 * 60),
	/**socket_session_tokens，保存1天 */
	SOCKET_SESSION_TOKENS("socket_session_tokens", 24 * 60 * 60),
	/** Token，保存1天 */
	TOKEN_API("token_api_", 24 * 60 * 60),
	/** 商家Token，保存1天 */
	TOKEN_API_STORE("token_store_", 8 * 60 * 60),
	/** 忘记登录密码图片验证码，保存10分钟 */
	FORGET_LOGIN_PIC_CODE("flpc_", 10 * 60),
	/** 图片验证码，保存10分钟 */
	PIC_CODE("pic_code_", 10 * 60),

	/** 短信验证码，保存10分钟 */
	SMS_CODE("sms_code_", 10 * 60),

	/** 首页广告列表，保存1分钟 */
	HOME_AD_LIST("home_ad_list", 1 * 60),

	/** 首页累计参与次数统计，保存1分钟 */
	HOME_PLAYERS_COUNT("home_players_count", 1 * 60),

	/** 首页最佳人气列表，保存1分钟 */
	HOME_HOT_LIST("home_hot_list", 1 * 60),

	/** 首页竞拍排行榜列表，保存1分钟 */
	HOME_TOP_LIST("home_top_list", 1 * 60),

	/** 银行列表，保存10分钟 */
	BANK_LIST("bank_list", 10 * 60),

	/** 商品分类列表，保存1分钟*/
	GOODS_SORT_LIST("goods_sort_list", 1 * 60),
	
	/** EP商品分类列表，保存1分钟*/
	EPGOODS_SORT_LIST("epgoods_sort_list", 1 * 60),

	/** 商品列表统计，保存1分钟 */
	GOODS_COUNT("goods_count_", 1 * 60),

	/** 用户赠送列表统计，保存1分钟 */
	USER_DONATE_COUNT("user_donate_count_", 1 * 60),

	/** 用户充值列表统计，保存1分钟 */
	USER_RECHARGE_COUNT("user_recharge_count_", 1 * 60),

	/** 用户兑换列表统计，保存1分钟 */
	USER_EXCHANGE_COUNT("user_exchange_count_", 1 * 60),

	/** 用户消息统计，保存1分钟 */
	MESSAGE_COUNT("message_count_", 1 * 60),
	
	/** 用户积分流水统计*/
	RECORD_COUNT("record_count_", 1 * 60),
	
	/** 用户积分流水合计*/
	RECORD_SUM("record_sum_", 1 * 60),

	/** 用户竞拍列表统计，保存1分钟 */
	USER_DRAW_COUNT("user_draw_count_", 1 * 60),

	/** 用户中拍列表统计，保存1分钟 */
	GOODS_WIN_COUNT("goods_win_count_", 1 * 60),
	
	/** 用户商铺收藏统计，保存1分钟 */
	USER_STORE_COLLECT("user_store_collect_", 1 * 60),
	
	/** 商铺统计，保存1分钟 */
	STORE_COUNT("store_count", 1 * 60),

	/** 商品秒杀竞拍统计 */
	WATCH_KEY("watch_key_", -1),

	/** 商品秒杀竞拍列表 */
	WATCH_LIST("watch_list_", -1),
	
	/**商铺添加商品防止连续点击限制5秒 */
	STORE_ADD_GOODS("store_add_goods_", 20),
	
	/**商家发货防止连续点击限制5秒 */
	STORE_EXPRESS_GOODS("store_express_goods_", 20),
	
	/**EP兑换防止连续点击限制5秒 */
	EPEXCHANGE_GOODS("epexchange_goods_", 20),
	
	/**微信支付回调*/
	WEIXIN_CALLBACK("handle_callback", 6),
	
	/**手动支付回调*/
	HANDLE_CALLBACK("handle_callback", 6),

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

	public static void main(String[] args) {


	}

}
