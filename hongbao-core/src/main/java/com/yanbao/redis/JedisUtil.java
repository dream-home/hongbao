package com.yanbao.redis;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ShardedJedisPool;

public class JedisUtil {

	private static final JedisUtil redis = new JedisUtil();

	private static Logger logger = Logger.getLogger(JedisUtil.class);

	/**
	 * 缓存生存时间
	 */
	private final int expire = 60000;
	/** 操作Key的方法 */
	public Keys KEYS;
	/** 对存储结构为String类型的操作 */
	public Strings STRINGS;
	/** 对存储结构为List类型的操作 */
	public Lists LISTS;
	/** 对存储结构为Set类型的操作 */
	public Sets SETS;
	/** 对存储结构为HashMap类型的操作 */
	public Hash HASH;
	/** 对存储结构为Set(排序的)类型的操作 */
	public SortSet SORTSET;

	private static JedisPool jedisPool = null;

	private ShardedJedisPool shardedJedisPool = null;

	private JedisUtil() {
	}

	static {
		init();
	}

	/**
	 * 构建redis连接池
	 *
	 * @return JedisPool
	 */
	public static void init() {
		ResourceBundle bundle = ResourceBundle.getBundle("redis");
		if (bundle == null) {
			throw new IllegalArgumentException("[redis.properties] is not found!");
		}
		if (jedisPool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			jedisPool = new JedisPool(config, bundle.getString("redisip"), Integer.valueOf(bundle.getString("redisport")), Integer.valueOf(bundle.getString("timeout")),
					bundle.getString("password"), Integer.valueOf(bundle.getString("database")));
			logger.info("redis connect success ! ! !");
		}
	}

	public JedisPool getPool() {
		return jedisPool;
	}

	public ShardedJedisPool getShardedJedisPool() {
		return shardedJedisPool;
	}

	/**
	 * 从jedis连接池中获取获取jedis对象
	 * 
	 * @return
	 */
	public static Jedis getJedis() {
		return jedisPool.getResource();
	}

	/**
	 * 获取JedisUtil实例
	 * 
	 * @return
	 */
	public static JedisUtil getInstance() {
		return redis;
	}

	/**
	 * 回收jedis
	 * 
	 * @param jedis
	 */
	@SuppressWarnings("deprecation")
	public static void returnJedis(Jedis jedis) {
		jedisPool.returnResource(jedis);
	}

	/**
	 * 设置过期时间
	 *
	 * @param key
	 * @param seconds
	 */
	public static void expire(String key, int seconds) {
		if (seconds <= 0) {
			return;
		}
		Jedis jedis = getJedis();
		jedis.expire(key, seconds);
		returnJedis(jedis);
	}

	/**
	 * 设置默认过期时间
	 *
	 * @param key
	 */
	public void expire(String key) {
		expire(key, expire);
	}

}
