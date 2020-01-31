package com.pjy.nchu2.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserEntity {

    private int stuId;//学号
    private String password;
    private String idNum;//身份证号
    private String avatar;//头像
    private String nickName;
    private String name;
    private String major;
    private String className;//班级
    private String gender;
    private int gender_code;
    private int nationality_code;//民族代码
    private String nationality;//民族
    private String native_place;//
    private String native_code;
    private String phone;
    private String QQ;
    private String email;

    public Map toMap() {
        HashMap dataMap = new HashMap();

        dataMap.put("stuId", getStuId());
        dataMap.put("name", getName());
//        dataMap.put("areaCode", getAreaCode());
        dataMap.put("phone", getPhone());
        dataMap.put("avatar", getAvatar());
        dataMap.put("nickname", getNickName());
//        dataMap.put("createTime", getCreateTime());
//        dataMap.put("createTimeStr", TimeUtils.getTimeStr(createTime));
//        dataMap.put("limited", isLimited());
//        dataMap.put("lastLoginTime", getLastLoginTime());
//        dataMap.put("lastLoginTimeStr", TimeUtils.getTimeStr(lastLoginTime));
//        dataMap.put("handSource", handSource);

        return dataMap;
    }


}
