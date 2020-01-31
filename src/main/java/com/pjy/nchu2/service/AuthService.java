package com.pjy.nchu2.service;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.pjy.nchu2.config.RedisDefine;
import com.pjy.nchu2.entity.UserEntity;
import com.pjy.nchu2.utils.JsonUtils;
import net.sf.json.util.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    /**
     * 创建 并 保存token 至 redis
     * @param user
     * @return
     */
    public String createTokenAndSave(UserEntity user) {
        String tokenStr = user.getStuId() + user.getPhone() + new Timestamp(System.currentTimeMillis()).toString();
        String token = DigestUtil.md5Hex(tokenStr);//Hutool中方法对token进行加密

        if (redisService.exists(RedisDefine.NCHU_TOKEN_KEY + user.getStuId())) {
            String oldToken = redisService.get(RedisDefine.NCHU_TOKEN_KEY + user.getStuId()).toString();//获取旧token
            redisService.remove(RedisDefine.NCHU_TOKEN_INFO_KEY + oldToken);//通过旧token清除redis储存的token信息
        }
        redisService.set(RedisDefine.NCHU_TOKEN_KEY + user.getStuId(), token, 3600 * 24 * 30);
        redisService.set(RedisDefine.NCHU_TOKEN_INFO_KEY + user.getStuId(), getUserInfoStr(user), 3600 * 24 * 30);

//        refreshTokenExpire(token);//刷新token过期时间
        return token;
    }

    /**
     * 用户信息 转成String （存至 Token）
     *
     * @param user
     * @return
     */
    public String getUserInfoStr(UserEntity user) {
        return new JSONObject(user.toMap()).toString();
    }

    /**
     * 获取token中的用户id
     *
     * @param token
     * @return
     */
    public String getIdInToken(String token) {
        if (redisService.exists(RedisDefine.NCHU_TOKEN_INFO_KEY + token)) {
            String userStr = redisService.get(RedisDefine.NCHU_TOKEN_INFO_KEY + token).toString();
            Map map = JsonUtils.parseJSONstr2Map(userStr);
            String id = map.get("stuId").toString();
            return id;
        }
        return null;
    }

    /**
     * 刷新token过期时间
     *
     * @param token
     */
    public void refreshTokenExpire(String token) {
        if (redisService.exists(RedisDefine.NCHU_TOKEN_INFO_KEY + token)) {
            String id = getIdInToken(token);
            redisService.expire(RedisDefine.NCHU_TOKEN_KEY + id, 3600 * 24 * 30);
            redisService.expire(RedisDefine.NCHU_TOKEN_INFO_KEY + id, 3600 * 24 * 30);
        }
    }
}
