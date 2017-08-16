package com.yanbao.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class Hash {

	/**
	 * 从hash中删除指定的存储
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            field 存储的名字
	 * @return 状态码，1成功，0失败
	 */
	public static long hdel(String key, String field) {
		Jedis jedis = JedisUtil.getJedis();
		long s = jedis.hdel(key, field);
		JedisUtil.returnJedis(jedis);
		return s;
	}

	public static long hdel(String key) {
		Jedis jedis = JedisUtil.getJedis();
		long s = jedis.del(key);
		JedisUtil.returnJedis(jedis);
		return s;
	}

	/**
	 * 测试hash中指定的存储是否存在
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            field 存储的名字
	 * @return 1存在，0不存在
	 */
	public static boolean hexists(String key, String field) {
		Jedis sjedis = JedisUtil.getJedis();
		boolean s = sjedis.hexists(key, field);
		JedisUtil.returnJedis(sjedis);
		return s;
	}

	/**
	 * 返回hash中指定存储位置的值
	 *
	 * @param String
	 *            key
	 * @param String
	 *            field 存储的名字
	 * @return 存储对应的值
	 */
	public static String hget(String key, String field) {
		Jedis sjedis = JedisUtil.getJedis();
		String s = sjedis.hget(key, field);
		JedisUtil.returnJedis(sjedis);
		return s;
	}

	public static byte[] hget(byte[] key, byte[] field) {
		Jedis sjedis = JedisUtil.getJedis();
		byte[] s = sjedis.hget(key, field);
		JedisUtil.returnJedis(sjedis);
		return s;
	}

	/**
	 * 以Map的形式返回hash中的存储和值
	 * 
	 * @param String
	 *            key
	 * @return Map<Strinig,String>
	 */
	public static Map<String, String> hgetAll(String key) {
		Jedis sjedis = JedisUtil.getJedis();
		Map<String, String> map = sjedis.hgetAll(key);
		JedisUtil.returnJedis(sjedis);
		return map;
	}

	/**
	 * 添加一个对应关系
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            field
	 * @param String
	 *            value
	 * @return 状态码 1成功，0失败，field已存在将更新，也返回0
	 */
	public static long hset(String key, String field, String value) {
		Jedis jedis = JedisUtil.getJedis();
		long s = jedis.hset(key, field, value);
		JedisUtil.returnJedis(jedis);
		return s;
	}

	public static long hset(String key, String field, byte[] value) {
		Jedis jedis = JedisUtil.getJedis();
		long s = jedis.hset(key.getBytes(), field.getBytes(), value);
		JedisUtil.returnJedis(jedis);
		return s;
	}

	/**
	 * 添加对应关系，只有在field不存在时才执行
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            field
	 * @param String
	 *            value
	 * @return 状态码 1成功，0失败field已存
	 */
	public static long hsetnx(String key, String field, String value) {
		Jedis jedis = JedisUtil.getJedis();
		long s = jedis.hsetnx(key, field, value);
		JedisUtil.returnJedis(jedis);
		return s;
	}

	/**
	 * 获取hash中value的集合
	 *
	 * @param String
	 *            key
	 * @return List<String>
	 */
	public static List<String> hvals(String key) {
		Jedis sjedis = JedisUtil.getJedis();
		List<String> list = sjedis.hvals(key);
		JedisUtil.returnJedis(sjedis);
		return list;
	}

	/**
	 * 在指定的存储位置加上指定的数字，存储位置的值必须可转为数字类型
	 *
	 * @param String
	 *            key
	 * @param String
	 *            field 存储位置
	 * @param String
	 *            long value 要增加的值,可以是负数
	 * @return 增加指定数字后，存储位置的值
	 */
	public static long hincrby(String key, String field, long value) {
		Jedis jedis = JedisUtil.getJedis();
		long s = jedis.hincrBy(key, field, value);
		JedisUtil.returnJedis(jedis);
		return s;
	}

	/**
	 * 返回指定hash中的所有存储名字,类似Map中的keySet方法
	 *
	 * @param String
	 *            key
	 * @return Set<String> 存储名称的集合
	 */
	public static Set<String> hkeys(String key) {
		Jedis sjedis = JedisUtil.getJedis();
		Set<String> set = sjedis.hkeys(key);
		JedisUtil.returnJedis(sjedis);
		return set;
	}

	/**
	 * 获取hash中存储的个数，类似Map中size方法
	 *
	 * @param String
	 *            key
	 * @return long 存储的个数
	 */
	public static long hlen(String key) {
		Jedis sjedis = JedisUtil.getJedis();
		long len = sjedis.hlen(key);
		JedisUtil.returnJedis(sjedis);
		return len;
	}

	/**
	 * 根据多个key，获取对应的value，返回List,如果指定的key不存在,List对应位置为null
	 *
	 * @param String
	 *            key
	 * @param String
	 *            ... fields 存储位置
	 * @return List<String>
	 */
	public static List<String> hmget(String key, String... fields) {
		Jedis sjedis = JedisUtil.getJedis();
		List<String> list = sjedis.hmget(key, fields);
		JedisUtil.returnJedis(sjedis);
		return list;
	}

	public static List<byte[]> hmget(byte[] key, byte[]... fields) {
		Jedis sjedis = JedisUtil.getJedis();
		List<byte[]> list = sjedis.hmget(key, fields);
		JedisUtil.returnJedis(sjedis);
		return list;
	}

	/**
	 * 添加对应关系，如果对应关系已存在，则覆盖
	 *
	 * @param Strin
	 *            key
	 * @param Map
	 *            <String,String> 对应关系
	 * @return 状态，成功返回OK
	 */
	public static String hmset(String key, Map<String, String> map) {
		Jedis jedis = JedisUtil.getJedis();
		String s = jedis.hmset(key, map);
		JedisUtil.returnJedis(jedis);
		return s;
	}

	/**
	 * 添加对应关系，如果对应关系已存在，则覆盖
	 *
	 * @param Strin
	 *            key
	 * @param Map
	 *            <String,String> 对应关系
	 * @return 状态，成功返回OK
	 */
	public static String hmset(byte[] key, Map<byte[], byte[]> map) {
		Jedis jedis = JedisUtil.getJedis();
		String s = jedis.hmset(key, map);
		JedisUtil.returnJedis(jedis);
		return s;
	}

	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
		}
		return null;
	}

	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
		}
		return null;
	}
}
