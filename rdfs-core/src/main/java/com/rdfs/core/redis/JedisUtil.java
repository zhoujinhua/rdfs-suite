package com.rdfs.core.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdfs.core.utils.RdfsUtils;
import com.rdfs.core.utils.PropertyUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {

	private static Logger logger = LoggerFactory.getLogger(JedisUtil.class);

	// 连接池对象
	private static JedisPool jedisPool;

	static {
		String host = PropertyUtil.getProperty("redis_host");
		int port = Integer.valueOf(PropertyUtil.getProperty("redis_port"));
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(PropertyUtil.getProperty("redis_maxTotal")));
        config.setMaxIdle(Integer.valueOf(PropertyUtil.getProperty("redis_maxIdle")));
        config.setMinIdle(Integer.valueOf(PropertyUtil.getProperty("redis_minIdle")));
        config.setMaxWaitMillis(Integer.valueOf(PropertyUtil.getProperty("redis_maxWaitMillis")));
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        //Idle时进行连接扫描
        config.setTestWhileIdle(true);
        //表示idle object evitor两次扫描之间要sleep的毫秒数
        config.setTimeBetweenEvictionRunsMillis(30000);
        //表示idle object evitor每次扫描的最多的对象数
        config.setNumTestsPerEvictionRun(10);
        //表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
        config.setMinEvictableIdleTimeMillis(60000);
		jedisPool = new JedisPool(config, host, port);
	}

    /**
     * 获取Jedis连接客户端
     * 
     * @return
     */
	public static Jedis getJedisClient() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		} catch (Exception e) {
			logger.error("获取Redis客户端连接失败。");
			e.printStackTrace();
		}
		if (jedis == null) {
			logger.warn("没有获取到Redis客户端连接。");
		}
		return jedis;
	}

	/**
	 * 安全回收资源
	 * 
	 * @param jedis
	 */
	public static void close(Jedis jedis) {
		try {
			jedisPool.returnResource(jedis);
		} catch (Exception e) {
			if (jedis.isConnected()) {
				jedis.quit();
				jedis.disconnect();
			}
		}
	}

	/**
	 * 设置字符串型数据
	 * 
	 * @param key
	 *            存储键
	 * @param value
	 *            存储值
	 * @param timeout
	 *            超时时间(单位：秒） 设置为0，则无时效性。
	 * @return
	 */
	public static void setString(String key, String value, int timeout) {
		if (RdfsUtils.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		Jedis jedis = null;
		try {
			jedis = getJedisClient();
			jedis.set(key, value);
			if (timeout > 0) {
				jedis.expire(key, timeout);
			}
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
			logger.error("操作Redis失败", e);
		} finally {
			close(jedis);
		}
	}
	
	/**
	 * 设置字符串型数据过期时间
	 * 
	 * @param key
	 *            存储键
	 * @param timeout
	 *            超时时间(单位：秒）
	 * @param key
	 */
	public static void exprString(String key, int timeout) {
		if (RdfsUtils.isEmpty(key)) {
			return;
		}
		Jedis jedis = null;
		try {
			jedis = getJedisClient();
			jedis.expire(key, timeout);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
			logger.error("操作Redis失败", e);
		} finally {
			close(jedis);
		}
	}

	/**
	 * 设置序列化对象数据
	 * 
	 * @param key
	 *            存储键
	 * @param value
	 *            存储值
	 * @param timeout
	 *            超时时间(单位：秒） 设置为0，则无时效性。
	 * @return
	 */
	public static void setObj(String key, byte[] value, int timeout) {
		if (RdfsUtils.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		Jedis jedis = null;
		try {
			jedis = getJedisClient();
			jedis.set(key.getBytes(), value);
			if (timeout > 0) {
				jedis.expire(key, timeout);
			}
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
			logger.error("操作Redis失败", e);
		} finally {
			close(jedis);
		}
	}

	/**
	 * 获取字符串型数据
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		if (RdfsUtils.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getJedisClient();
			value = jedis.get(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
			logger.error("操作Redis失败", e);
		} finally {
			close(jedis);
		}
		return value;
	}

	/**
	 * 获取序列化对象数据
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] getObj(String key) {
		if (RdfsUtils.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		byte[] value = null;
		Jedis jedis = null;
		try {
			jedis = getJedisClient();
			value = jedis.get(key.getBytes());
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
			logger.error("操作Redis失败", e);
		} finally {
			close(jedis);
		}
		return value;
	}

	/**
	 * 删除对象数据
	 * 
	 * @param key
	 */
	public static void delObj(String key) {
		if (RdfsUtils.isEmpty(key)) {
			return;
		}
		Jedis jedis = null;
		try {
			jedis = getJedisClient();
			jedis.del(key.getBytes());
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
			logger.error("操作Redis失败", e);
		} finally {
			close(jedis);
		}
	}

	/**
	 * 删除字符串数据
	 * 
	 * @param key
	 */
	public static void delString(String key) {
		if (RdfsUtils.isEmpty(key)) {
			return;
		}
		Jedis jedis = null;
		try {
			jedis = getJedisClient();
			jedis.del(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
			logger.error("操作Redis失败", e);
		} finally {
			close(jedis);
		}
	}
	
	/**
	 * 清除DB
	 * 
	 * @param key
	 */
	public static void flushDB() {
		Jedis jedis = null;
		try {
			jedis = getJedisClient();
			jedis.flushDB();
			logger.info("Redsi缓存DB重置成功。");
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
			logger.error("操作Redis失败", e);
		} finally {
			close(jedis);
		}
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		flushDB();
	}

}
