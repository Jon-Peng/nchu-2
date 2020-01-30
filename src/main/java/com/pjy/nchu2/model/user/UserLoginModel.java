package com.pjy.nchu2.model.user;

import lombok.Data;

@Data
public class UserLoginModel {
    private int stuId;
    private String password;

    @Override
    public String toString() {
        return "UserLoginModel{" +
                "stuId='" + stuId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
