package com.server.redis;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.server.module.config.KeyPrefix;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
@Component
public class RedisClient {
	@Autowired
	private RedisUtils redisUtils;

	     /** 
	     * 通过key删除（字节） 
	     * @param key 
	     */  
	    public void del(byte [] key){  
	        Jedis jedis = redisUtils.getJedis();  
	        jedis.del(key);  
	        redisUtils.returnResource(jedis);  
	    }  
	    /** 
	     * 通过key删除 
	     * @param key 
	     */  
	    public void del(String key){  
	        Jedis jedis = redisUtils.getJedis();  
	        jedis.del(key);  
	        redisUtils.returnResource(jedis);  
	    }  
	  
	    /** 
	     * 添加key value 并且设置存活时间(byte) 
	     * @param key 
	     * @param value 
	     * @param liveTime 
	     */  
	    public void set(byte [] key,byte [] value,int liveTime){  
	        Jedis jedis = redisUtils.getJedis();  
	        jedis.set(key, value);  
	        jedis.expire(key, liveTime);  
	        redisUtils.returnResource(jedis);  
	    }  
	    /** 
	     * 添加key value 并且设置存活时间 
	     * @param key 
	     * @param value 
	     * @param liveTime 
	     */  
	    public void set(String key,String value,int liveTime){  
	        Jedis jedis = redisUtils.getJedis();  
	        jedis.set(key, value);  
	        jedis.expire(key, liveTime);  
	        redisUtils.returnResource(jedis);  
	    }  
	    /** 
	     * 添加key value 
	     * @param key 
	     * @param value 
	     */  
	    public void set(String key,String value){  
	        Jedis jedis = redisUtils.getJedis();  
	        jedis.set(key, value);  
	        redisUtils.returnResource(jedis);  
	    }  
	    
	    public  boolean set(KeyPrefix prefix, String key,  String content) {
			 Jedis jedis = null;
			 try {
				 jedis =  redisUtils.getJedis();  
				 if(content == null || content.length() <= 0) {
					 return false;
				 }
				//生成真正的key
				 String realKey  = prefix.getPrefix() + key;
				 int seconds =  prefix.expireSeconds();
				 if(seconds <= 0) {
					 jedis.set(realKey, content);
				 }else {
					 jedis.setex(realKey, seconds, content);
				 }
				 return true;
			 }finally {
				 redisUtils.returnResource(jedis);  
			 }
		}
	    /**添加key value (字节)(序列化) 
	     * @param key 
	     * @param value 
	     */  
	    public void set(byte [] key,byte [] value){  
	        Jedis jedis = redisUtils.getJedis();  
	        jedis.set(key, value);  
	        redisUtils.returnResource(jedis);  
	    }  
	    
	    
	    /** 
	     * 获取redis value (String) 
	     * @param key 
	     * @return 
	     */  
	    public String get(String key){  
	        Jedis jedis = redisUtils.getJedis();  
	         String value = jedis.get(key);  
	        redisUtils.returnResource(jedis);  
	        return value;  
	    }  
	    
	    public String get(KeyPrefix prefix, String key) {
			 Jedis jedis = null;
			 try {
				 jedis =  redisUtils.getJedis();  
				 //生成真正的key
				 String realKey  = prefix.getPrefix() + key;
				 String  str = jedis.get(realKey);
				 return str;
			 }finally {
				 redisUtils.returnResource(jedis);  
			 }
		}
	    
	
	    /** 
	     * 获取redis value (byte [] )(反序列化) 
	     * @param key 
	     * @return 
	     */  
	    public byte[] get(byte [] key){  
	        Jedis jedis = redisUtils.getJedis();  
	        byte[] value = jedis.get(key);  
	        redisUtils.returnResource(jedis);  
	        return value;  
	    }  
	  
	    /**
		 * 获取当个对象
		 * */
		public <T> T get(String key,  Class<T> clazz) {
			 Jedis jedis = null;
			 try {
				 jedis =  redisUtils.getJedis();
				 //生成真正的key
				 String  str = jedis.get(key);
				 T t =  stringToBean(str, clazz);
				 return t;
			 }finally {
				 redisUtils.returnResource(jedis);  
			 }
		}
		
		/**
		 * 设置对象
		 * */
		public <T> boolean set(String key,  T value ,int expires) {
			 Jedis jedis = null;
			 try {
				 jedis =  redisUtils.getJedis();
				 String str = beanToString(value);
				 if(str == null || str.length() <= 0) {
					 return false;
				 }
				//生成真正的key
				 if(expires <= 0) {
					 jedis.set(key, str);
				 }else {
					 jedis.setex(key, expires, str);
				 }
				 return true;
			 }finally {
				 redisUtils.returnResource(jedis);  
			 }
		}
	    /** 
	     * 通过正则匹配keys 
	     * @param pattern 
	     * @return 
	     */  
	    public Set<String> keys(String pattern){  
	        Jedis jedis = redisUtils.getJedis();  
	        Set<String> value = jedis.keys(pattern);  
	        redisUtils.returnResource(jedis);  
	        return value;  
	    }  
	  
	    /** 
	     * 检查key是否已经存在 
	     * @param key 
	     * @return 
	     */  
	    public boolean exists(String key){  
	        Jedis jedis = redisUtils.getJedis();  
	        boolean value = jedis.exists(key);  
	        redisUtils.returnResource(jedis);  
	        return value;  
	    }  
	      
	    /*******************redis list操作************************/  
	    /** 
	     * 往list中添加元素 
	     * @param key 
	     * @param value 
	     */  
	    public void lpush(String key,String value){  
	        Jedis jedis = redisUtils.getJedis();  
	        jedis.lpush(key, value);  
	        redisUtils.returnResource(jedis);  
	    }  
	      
	    public void rpush(String key,String value){  
	        Jedis jedis = redisUtils.getJedis();  
	        jedis.rpush(key, value);  
	        redisUtils.returnResource(jedis);  
	    }  
	      
	    /** 
	     * 数组长度 
	     * @param key 
	     * @return 
	     */  
	    public Long llen(String key){  
	        Jedis jedis = redisUtils.getJedis();  
	        Long len = jedis.llen(key);  
	        redisUtils.returnResource(jedis);  
	        return len;  
	    }  
	      
	    /** 
	     * 获取下标为index的value 
	     * @param key 
	     * @param index 
	     * @return 
	     */  
	    public String lindex(String key,Long index){  
	        Jedis jedis = redisUtils.getJedis();  
	        String str = jedis.lindex(key, index);  
	        redisUtils.returnResource(jedis);  
	        return str;  
	    }  
	      
	    public String lpop(String key){  
	        Jedis jedis = redisUtils.getJedis();  
	        String str = jedis.lpop(key);  
	        redisUtils.returnResource(jedis);  
	        return str;  
	    }  
	      
	    public List<String> lrange(String key,long start,long end){  
	        Jedis jedis = redisUtils.getJedis();  
	        List<String> str = jedis.lrange(key, start, end);  
	        redisUtils.returnResource(jedis);  
	        return str;  
	    }  
	    /*********************redis list操作结束**************************/  
	      
	    /** 
	     * 清空redis 所有数据 
	     * @return 
	     */  
	    public String flushDB(){  
	        Jedis jedis = redisUtils.getJedis();  
	        String str = jedis.flushDB();  
	        redisUtils.returnResource(jedis);  
	        return str;  
	    }  
	    /** 
	     * 查看redis里有多少数据 
	     */  
	    public long dbSize(){  
	        Jedis jedis = redisUtils.getJedis();  
	        long len = jedis.dbSize();  
	        redisUtils.returnResource(jedis);  
	        return len;  
	    }  
	    /** 
	     * 检查是否连接成功 
	     * @return 
	     */  
	    public String ping(){  
	        Jedis jedis = redisUtils.getJedis();  
	        String str = jedis.ping();  
	        redisUtils.returnResource(jedis);  
	        return str;  
	    }  
	    /**
	     * 自增加1
	     */
	    public Long incr(String key) {
	    	Jedis jedis = redisUtils.getJedis(); 
	    	Long incr = jedis.incr(key);
	    	return incr;
	    }

	    @SuppressWarnings("unchecked")
		private <T> T stringToBean(String str, Class<T> clazz) {
			if(str == null || str.length() <= 0 || clazz == null) {
				 return null;
			}
			if(clazz == int.class || clazz == Integer.class) {
				 return (T)Integer.valueOf(str);
			}else if(clazz == String.class) {
				 return (T)str;
			}else if(clazz == long.class || clazz == Long.class) {
				return  (T)Long.valueOf(str);
			}else {
				return JSON.toJavaObject(JSON.parseObject(str), clazz);
			}
		}
	    
	    private <T> String beanToString(T value) {
			if(value == null) {
				return null;
			}
			Class<?> clazz = value.getClass();
			if(clazz == int.class || clazz == Integer.class) {
				 return ""+value;
			}else if(clazz == String.class) {
				 return (String)value;
			}else if(clazz == long.class || clazz == Long.class) {
				return ""+value;
			}else {
				return JSON.toJSONString(value);
			}
		}
		
}  

