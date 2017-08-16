package com.yanbao.redis;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.util.SafeEncoder;

public class Strings {

	public static int minute = 60;
	public static int hour = 60 * 60;
	public static int day = 60 * 60 * 24;

	/**
	 * 根据key获取记录
	 * 
	 * @param String
	 *            key
	 * @return 值
	 */
	public static String get(String key) {
		Jedis sjedis = JedisUtil.getJedis();
		String value = sjedis.get(key);
		JedisUtil.returnJedis(sjedis);
		return value;
	}

	/**
	 * 根据key获取记录
	 * 
	 * @param byte[] key
	 * @return 值
	 */
	public static byte[] get(byte[] key) {
		Jedis sjedis = JedisUtil.getJedis();
		byte[] value = sjedis.get(key);
		JedisUtil.returnJedis(sjedis);
		return value;
	}

	/**
	 * 删除
	 * 
	 * @param key
	 * @return
	 */
	public static Long del(String key) {
		Jedis sjedis = JedisUtil.getJedis();
		Long count = sjedis.del(key);
		JedisUtil.returnJedis(sjedis);
		return count;
	}

	/**
	 * 添加有过期时间的记录
	 *
	 * @param String
	 *            key
	 * @param int seconds 过期时间，以秒为单位
	 * @param String
	 *            value
	 * @return String 操作状态
	 */
	public static String setEx(String key, int seconds, String value) {
		Jedis jedis = JedisUtil.getJedis();
		String str = jedis.setex(key, seconds, value);
		JedisUtil.returnJedis(jedis);
		return str;
	}

	/**
	 * 添加有过期时间的记录
	 *
	 * @param String
	 *            key
	 * @param int seconds  设置过期时间，以秒为单位
	 * @param String
	 *            value
	 * @return String 操作状态
	 */
	public static Long expire(String key, int seconds) {
		Jedis jedis = JedisUtil.getJedis();
		Long str = jedis.expire(key, seconds);
		JedisUtil.returnJedis(jedis);
		return str;
	}

	/**
	 * 添加有过期时间的记录
	 *
	 * @param String
	 *            key
	 * @param int seconds 过期时间，以秒为单位
	 * @param String
	 *            value
	 * @return String 操作状态
	 */
	public static String setEx(byte[] key, int seconds, byte[] value) {
		Jedis jedis = JedisUtil.getJedis();
		String str = jedis.setex(key, seconds, value);
		JedisUtil.returnJedis(jedis);
		return str;
	}

	/**
	 * 添加一条记录，仅当给定的key不存在时才插入
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return long 状态码，1插入成功且key不存在，0未插入，key存在
	 */
	public static long setnx(String key, String value) {
		Jedis jedis = JedisUtil.getJedis();
		long str = jedis.setnx(key, value);
		JedisUtil.returnJedis(jedis);
		return str;
	}

	/**
	 * 添加记录,如果记录已存在将覆盖原有的value
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return 状态码
	 */
	public static String set(String key, String value) {
		return set(SafeEncoder.encode(key), SafeEncoder.encode(value));
	}

	/**
	 * 添加记录,如果记录已存在将覆盖原有的value
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return 状态码
	 */
	public static String set(String key, byte[] value) {
		return set(SafeEncoder.encode(key), value);
	}

	/**
	 * 添加记录,如果记录已存在将覆盖原有的value
	 * 
	 * @param byte[] key
	 * @param byte[] value
	 * @return 状态码
	 */
	public static String set(byte[] key, byte[] value) {
		Jedis jedis = JedisUtil.getJedis();
		String status = jedis.set(key, value);
		JedisUtil.returnJedis(jedis);
		return status;
	}

	/**
	 * 从指定位置开始插入数据，插入的数据会覆盖指定位置以后的数据<br/>
	 * 例:String str1="123456789";<br/>
	 * 对str1操作后setRange(key,4,0000)，str1="123400009";
	 * 
	 * @param String
	 *            key
	 * @param long offset
	 * @param String
	 *            value
	 * @return long value的长度
	 */
	public static long setRange(String key, long offset, String value) {
		Jedis jedis = JedisUtil.getJedis();
		long len = jedis.setrange(key, offset, value);
		JedisUtil.returnJedis(jedis);
		return len;
	}

	/**
	 * 在指定的key中追加value
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return long 追加后value的长度
	 */
	public static long append(String key, String value) {
		Jedis jedis = JedisUtil.getJedis();
		long len = jedis.append(key, value);
		JedisUtil.returnJedis(jedis);
		return len;
	}

	/**
	 * 将key对应的value减去指定的值，只有value可以转为数字时该方法才可用
	 *
	 * @param String
	 *            key
	 * @param long number 要减去的值
	 * @return long 减指定值后的值
	 */
	public static long decrBy(String key, long number) {
		Jedis jedis = JedisUtil.getJedis();
		long len = jedis.decrBy(key, number);
		JedisUtil.returnJedis(jedis);
		return len;
	}

	/**
	 * <b>可以作为获取唯一id的方法</b><br/>
	 * 将key对应的value加上指定的值，只有value可以转为数字时该方法才可用
	 * 
	 * @param String
	 *            key
	 * @param long number 要减去的值
	 * @return long 相加后的值
	 */
	public static long incrBy(String key, long number) {
		Jedis jedis = JedisUtil.getJedis();
		long len = jedis.incrBy(key, number);
		JedisUtil.returnJedis(jedis);
		return len;
	}

	/**
	 * 对指定key对应的value进行截取
	 * 
	 * @param String
	 *            key
	 * @param long startOffset 开始位置(包含)
	 * @param long endOffset 结束位置(包含)
	 * @return String 截取的值
	 */
	public static String getrange(String key, long startOffset, long endOffset) {
		Jedis sjedis = JedisUtil.getJedis();
		String value = sjedis.getrange(key, startOffset, endOffset);
		JedisUtil.returnJedis(sjedis);
		return value;
	}

	/**
	 * 获取并设置指定key对应的value<br/>
	 * 如果key存在返回之前的value,否则返回null
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return String 原始value或null
	 */
	public static String getSet(String key, String value) {
		Jedis jedis = JedisUtil.getJedis();
		String str = jedis.getSet(key, value);
		JedisUtil.returnJedis(jedis);
		return str;
	}

	/**
	 * 批量获取记录,如果指定的key不存在返回List的对应位置将是null
	 * 
	 * @param String
	 *            keys
	 * @return List<String> 值得集合
	 */
	public static List<String> mget(String... keys) {
		Jedis jedis = JedisUtil.getJedis();
		List<String> str = jedis.mget(keys);
		JedisUtil.returnJedis(jedis);
		return str;
	}

	/**
	 * 批量存储记录
	 * 
	 * @param String
	 *            keysvalues 例:keysvalues="key1","value1","key2","value2";
	 * @return String 状态码
	 */
	public static String mset(String... keysvalues) {
		Jedis jedis = JedisUtil.getJedis();
		String str = jedis.mset(keysvalues);
		JedisUtil.returnJedis(jedis);
		return str;
	}

	/**
	 * 获取key对应的值的长度
	 * 
	 * @param String
	 *            key
	 * @return value值得长度
	 */
	public static long strlen(String key) {
		Jedis jedis = JedisUtil.getJedis();
		long len = jedis.strlen(key);
		JedisUtil.returnJedis(jedis);
		return len;
	}
}