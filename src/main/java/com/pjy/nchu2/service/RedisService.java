package com.pjy.nchu2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class RedisService {

    //未使用
//    public static final String K_SMS_VERIFY_CODE_SIGN_UP = "66-community:sign_up_verify_code:";
//    public static final String K_MEMBER_AUTH_TOKEN = "66-community:auth_token:";
//    public static final String K_MANAGER_AUTH_TOKEN = "66-community:manager_auth_token:";
//    public static final String K_WEB_AUTH_TOKEN = "66-community:web_auth_token:";
//    public static final String K_POKER_STAR_COOKIE_DATA = "66-community:texas:data";
//    public static final String K_SIXUP_AUTH_TOKEN = "6up_token:";
//    public static final String K_SIXUP_AUTH_TOKEN_INFO = "6up_tokenInfo:";


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }

    public RedisService() {
    }

    public boolean set(String key, Object value) {
        boolean result = false;

        try {
            ValueOperations<Serializable, Object> operations = this.redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return result;
    }

    public boolean set(String key, Object value, long expireTime) {
        return set(key, value, Long.valueOf(expireTime),TimeUnit.SECONDS);
    }

    public boolean set(final String key, final Object value,final Long expireTime,final TimeUnit unit) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = this.redisTemplate.opsForValue();
            operations.set(key, value);
            this.redisTemplate.expire(key, expireTime, unit);
            result = true;
        } catch (Exception var6) {
            var6.printStackTrace();
        }
        return result;
    }

    public void remove(String... keys) {
        String[] var2 = keys;
        int var3 = keys.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String key = var2[var4];
            this.remove(key);
        }

    }

    public void removePattern(String pattern) {
        Set<Serializable> keys = this.redisTemplate.keys(pattern);
        if(keys.size() > 0) {
            this.redisTemplate.delete(keys);
        }

    }

    public void remove(String key) {
        if(this.exists(key)) {
            this.redisTemplate.delete(key);
        }

    }

    public boolean exists(String key) {
        return this.redisTemplate.hasKey(key).booleanValue();
    }

    public Long increment(String key, long n, long timeout) {
        Long m = this.redisTemplate.opsForValue().increment(key, n);
        expire(key, timeout);
        return m;
    }

    public Long increment(String key, long n) {
        Long m = this.redisTemplate.opsForValue().increment(key, n);
        return m;
    }

    public Object get(String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = this.redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    public void hmSet(String key, Object hashKey, Object value, long timeout) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
        expire(key, timeout);
    }

    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    public Set<Object> hmKeys(String key) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        return hash.keys(key);
    }

    public List<Object> hmValues(String key) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        return hash.values(key);
    }

    public void hmDel(String masterKey, Object... hashKey) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        hash.delete(masterKey, hashKey);
    }

    public Long hmIncre(String masterKey, Object hashKey,long n) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        return hash.increment(masterKey, hashKey, n);
    }

    public void lPush(String k, Object v) {
        ListOperations<String, Object> list = this.redisTemplate.opsForList();
        list.rightPush(k, v);
    }

    public List<Object> lRange(String k, long l, long l1) {
        ListOperations<String, Object> list = this.redisTemplate.opsForList();
        return list.range(k, l, l1);
    }

    public void add(String key, Object value) {
        SetOperations<String, Object> set = this.redisTemplate.opsForSet();
        set.add(key, new Object[]{value});
    }

    public void setBatchAdd(String key, Object[] value) {
        SetOperations<String, Object> set = this.redisTemplate.opsForSet();
        set.add(key, value);
    }

    public Set<Object> setMembers(String key) {
        SetOperations<String, Object> set = this.redisTemplate.opsForSet();
        return set.members(key);
    }

    public Set<String> setMembersOfString(String key) {
        SetOperations<String, String> set = this.redisTemplate.opsForSet();
        return set.members(key);
    }

    public void zAdd(String key, Object value, double scoure) {
        ZSetOperations<String, Object> zset = this.redisTemplate.opsForZSet();
        zset.add(key, value, scoure);
    }

    public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
        ZSetOperations<String, Object> zset = this.redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }

    public Object lPop(String k) {
        ListOperations<String, Object> list = this.redisTemplate.opsForList();
        return list.leftPop(k);
    }

    public Object spop(String key) {
        SetOperations<String, Object> set = this.redisTemplate.opsForSet();
        return set.pop(key);
    }

    public Long setSize(String key) {
        SetOperations<String, Object> set = this.redisTemplate.opsForSet();
        return set.size(key);
    }

    public boolean expire(String key, long timeout) {
        return this.redisTemplate.expire(key, timeout, TimeUnit.SECONDS).booleanValue();
    }

    public boolean expire(String key, long timeout,TimeUnit unit) {
        return this.redisTemplate.expire(key, timeout, unit).booleanValue();
    }

    public long ttl(String key) {
        return this.redisTemplate.getExpire(key).longValue();
    }

    public Long getListSize(String key) {
        ListOperations<String, Object> list = this.redisTemplate.opsForList();
        return list.size(key);
    }

    public Object lindex(String key, long index) {
        ListOperations<String, Object> list = this.redisTemplate.opsForList();
        return list.index(key, index);
    }

    public Boolean isMember(String key,Object o) {
        SetOperations<String, Object> set = this.redisTemplate.opsForSet();
        return set.isMember(key, o);
    }

    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String key, long start, long end) {
        ZSetOperations<String, Object> zset = this.redisTemplate.opsForZSet();
        return zset.reverseRangeWithScores(key, start, end);
    }

    public void removeMember(String key, Object... values) {
        SetOperations<String, Object> set = this.redisTemplate.opsForSet();
        set.remove(key, values);
    }

}