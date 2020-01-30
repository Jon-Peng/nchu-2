package com.pjy.nchu2.service;

import com.pjy.nchu2.config.RedisDefine;
import com.pjy.nchu2.entity.UserEntity;
import com.pjy.nchu2.mapper.UserMapper;
import com.pjy.nchu2.model.user.UserLoginModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    //查找账户
    public UserEntity login(UserLoginModel userLoginModel){

        return userMapper.accountLogin(userLoginModel);

    }

    //setPassword
    public void setPassword(UserEntity userEntity ){
         userMapper.setPassword(userEntity);
    }

    public void firstSet(UserEntity userEntity) {
        userMapper.firstSet(userEntity);
    }

    //id获取用户
    public UserEntity getUser(int id) {

        return userMapper.selectOneUser( id);
    }

    //request获取用户
    public UserEntity getUser(HttpServletRequest request) {
//        token = request.getHeader(tokenName);
//        redisKey = RedisDefine.K_COMMUNITY_TOKEN_INFO_KEY + token;
        return null;
    }

    /**
     * 检查用户名是否已存在
     * @param username
     * @return
     */
    public boolean checkUsernameExists(String username) {

        return false;
    }

    /**
     * 核对验证码是否正确
     * @param email
     * @param verifivationCode
     * @return
     */
    public boolean checkVerificationCode(String email, String verifivationCode) {

        String redisKey = RedisDefine.NCHU_EMAIL_VERIFY_KEY+email;

        if (redisService.exists(redisKey)){
            if (redisService.get(redisKey).equals(verifivationCode)){
                return true;
            }
        }
        return false;
    }

    /**
     * 创建用户
     * @param username
     * @param password
     * @param email
     * @return
     */
    public UserEntity createUser(String username, String password, String email) {

        return null;
    }

    /**
     * 將驗證碼存入redis
     * @param email
     * @param code
     */
    public void saveVerifyCode(String email, String code) {
        redisService.set(RedisDefine.NCHU_EMAIL_VERIFY_KEY+email,code, 10*60);
    }
}
