package com.yanbao.redis;

import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;
import redis.clients.util.SafeEncoder;

public class Keys {

	/**
	 * 清空所有key
	 */
	public String flushAll() {
		Jedis jedis = JedisUtil.getJedis();
		String stata = jedis.flushAll();
		JedisUtil.returnJedis(jedis);
		return stata;
	}

	/**
	 * 更改key
	 *
	 * @param String
	 *            oldkey
	 * @param String
	 *            newkey
	 * @return 状态码
	 * */
	public String rename(String oldkey, String newkey) {
		return rename(SafeEncoder.encode(oldkey), SafeEncoder.encode(newkey));
	}

	/**
	 * 更改key,仅当新key不存在时才执行
	 *
	 * @param String
	 *            oldkey
	 * @param String
	 *            newkey
	 * @return 状态码
	 * */
	public long renamenx(String oldkey, String newkey) {
		Jedis jedis = JedisUtil.getJedis();
		long status = jedis.renamenx(oldkey, newkey);
		JedisUtil.returnJedis(jedis);
		return status;
	}

	/**
	 * 更改key
	 *
	 * @param String
	 *            oldkey
	 * @param String
	 *            newkey
	 * @return 状态码
	 * */
	public String rename(byte[] oldkey, byte[] newkey) {
		Jedis jedis = JedisUtil.getJedis();
		String status = jedis.rename(oldkey, newkey);
		JedisUtil.returnJedis(jedis);
		return status;
	}

	/**
	 * 设置key的过期时间，以秒为单位
	 *
	 * @param String
	 *            key
	 * @param 时间
	 *            ,已秒为单位
	 * @return 影响的记录数
	 * */
	public long expired(String key, int seconds) {
		Jedis jedis = JedisUtil.getJedis();
		long count = jedis.expire(key, seconds);
		JedisUtil.returnJedis(jedis);
		return count;
	}

	/**
	 * 设置key的过期时间,它是距历元（即格林威治标准时间 1970 年 1 月 1 日的 00:00:00，格里高利历）的偏移量。
	 *
	 * @param String
	 *            key
	 * @param 时间
	 *            ,已秒为单位
	 * @return 影响的记录数
	 * */
	public long expireAt(String key, long timestamp) {
		Jedis jedis = JedisUtil.getJedis();
		long count = jedis.expireAt(key, timestamp);
		JedisUtil.returnJedis(jedis);
		return count;
	}

	/**
	 * 查询key的过期时间
	 *
	 * @param String
	 *            key
	 * @return 以秒为单位的时间表示
	 * */
	public long ttl(String key) {
		Jedis sjedis = JedisUtil.getJedis();
		long len = sjedis.ttl(key);
		JedisUtil.returnJedis(sjedis);
		return len;
	}

	/**
	 * 取消对key过期时间的设置
	 *
	 * @param key
	 * @return 影响的记录数
	 * */
	public long persist(String key) {
		Jedis jedis = JedisUtil.getJedis();
		long count = jedis.persist(key);
		JedisUtil.returnJedis(jedis);
		return count;
	}

	/**
	 * 删除keys对应的记录,可以是多个key
	 *
	 * @param String
	 *            ... keys
	 * @return 删除的记录数
	 * */
	public long del(String... keys) {
		Jedis jedis = JedisUtil.getJedis();
		long count = jedis.del(keys);
		JedisUtil.returnJedis(jedis);
		return count;
	}

	/**
	 * 删除keys对应的记录,可以是多个key
	 *
	 * @param String
	 *            ... keys
	 * @return 删除的记录数
	 * */
	public long del(byte[]... keys) {
		Jedis jedis = JedisUtil.getJedis();
		long count = jedis.del(keys);
		JedisUtil.returnJedis(jedis);
		return count;
	}

	/**
	 * 判断key是否存在
	 *
	 * @param String
	 *            key
	 * @return boolean
	 * */
	public static boolean exists(String key) {
		Jedis sjedis = JedisUtil.getJedis();
		boolean exis = sjedis.exists(key);
		JedisUtil.returnJedis(sjedis);
		return exis;
	}

	/**
	 * 对List,Set,SortSet进行排序,如果集合数据较大应避免使用这个方法
	 *
	 * @param String
	 *            key
	 * @return List<String> 集合的全部记录
	 */
	public List<String> sort(String key) {
		Jedis sjedis = JedisUtil.getJedis();
		List<String> list = sjedis.sort(key);
		JedisUtil.returnJedis(sjedis);
		return list;
	}

	/**
	 * 对List,Set,SortSet进行排序或limit
	 *
	 * @param String
	 *            key
	 * @param SortingParams
	 *            parame 定义排序类型或limit的起止位置.
	 * @return List<String> 全部或部分记录
	 */
	public List<String> sort(String key, SortingParams parame) {
		Jedis sjedis = JedisUtil.getJedis();
		List<String> list = sjedis.sort(key, parame);
		JedisUtil.returnJedis(sjedis);
		return list;
	}

	/**
	 * 返回指定key存储的类型
	 *
	 * @param String
	 *            key
	 * @return String string|list|set|zset|hash
	 */
	public String type(String key) {
		Jedis sjedis = JedisUtil.getJedis();
		String type = sjedis.type(key);
		JedisUtil.returnJedis(sjedis);
		return type;
	}

	/**
	 * 查找所有匹配给定的模式的键
	 *
	 * @param String
	 *            key的表达式,*表示多个，？表示一个
	 */
	public Set<String> keys(String pattern) {
		Jedis jedis = JedisUtil.getJedis();
		Set<String> set = jedis.keys(pattern);
		JedisUtil.returnJedis(jedis);
		return set;
	}
}
