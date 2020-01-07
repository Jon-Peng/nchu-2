package com.pjy.nchu2.service;

import com.pjy.nchu2.entity.UserEntity;
import com.pjy.nchu2.mapper.UserMapper;
import com.pjy.nchu2.model.UserLoginModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    //查找账户
    public UserEntity login(UserLoginModel userLoginModel){

        System.out.println("--accountLogin--"+userMapper.accountLogin(userLoginModel));
        return userMapper.accountLogin(userLoginModel);

    }

    //setPassword
    public void setPassword(UserEntity userEntity ){
         userMapper.setPassword(userEntity);
    }
    public void setNativeCode(UserEntity userEntity ){
         userMapper.setNativeCode(userEntity);
    }

    public void firstSet(UserEntity userEntity) {
        userMapper.firstSet(userEntity);
    }

    //
    public UserEntity getUser(int id) {

        return userMapper.selectOneUser( id);
    }


}
