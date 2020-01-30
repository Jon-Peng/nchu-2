package com.pjy.nchu2.mapper;

import com.pjy.nchu2.entity.UserEntity;
import com.pjy.nchu2.model.user.UserLoginModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    //setPassword
    void setPassword(UserEntity userEntity);
    //setNativeCode
    void setNativeCode(UserEntity userEntity);
    //添加账户
    void insert(UserEntity userEntity);
    //更改账户
    void update(UserEntity userEntity);
    //删除账户
    void delete(UserEntity userEntity);

    //查找单个账户
    UserEntity accountLogin(UserLoginModel userLoginModel);
    UserEntity selectOneUser(int id);
    //map查询
    List<UserEntity> select(Map params);


    void firstSet(UserEntity userEntity);
}
