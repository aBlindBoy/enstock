package com.xc.utils.redis;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import redis.clients.jedis.ShardedJedis;


public class RedisShardedPoolUtils {

    private static final Logger log = LoggerFactory.getLogger(RedisShardedPoolUtils.class);

    /**
     * 設置key的有效期，單位是秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime){
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
            log.error("expire key:{} error",key,e);
            RedisShardedPool.close(jedis);
            return result;
        }
        RedisShardedPool.close(jedis);
        return result;
    }

    //exTime的單位是秒
    public static String setEx(String key,String value,int exTime){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} error",key,value,e);
            RedisShardedPool.close(jedis);
            return result;
        }
        RedisShardedPool.close(jedis);
        return result;
    }

    public static String set(String key,String value){
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e);
            RedisShardedPool.close(jedis);
            return result;
        }
        RedisShardedPool.close(jedis);
        return result;
    }

    public static String get(String key){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error",key,e);
            RedisShardedPool.close(jedis);
            return result;
        }
        RedisShardedPool.close(jedis);
        return result;
    }

    public static Long del(String key){
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error",key,e);
            RedisShardedPool.close(jedis);
            return result;
        }
        RedisShardedPool.close(jedis);
        return result;
    }


    public static String getSet(String key,String value) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.getSet(key,value);
        } catch (Exception e) {
            log.error("getset key:{} error", key, e);
            RedisShardedPool.close(jedis);
            return result;
        }
        RedisShardedPool.close(jedis);
        return result;
    }

    public static Long setnx(String key, String value) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            log.error("setnx key:{} value:{} error", key, value, e);
            RedisShardedPool.close(jedis);
            return result;
        }
        RedisShardedPool.close(jedis);
        return result;
    }


    public static void main(String[] args) {
        ShardedJedis jedis = RedisShardedPool.getJedis();

        System.out.println(jedis.get("key1"));

        System.out.println("end");


    }

}