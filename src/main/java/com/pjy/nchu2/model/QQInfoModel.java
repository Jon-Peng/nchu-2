package com.pjy.nchu2.model;

import lombok.Data;

@Data
public class QQInfoModel {
    public int code;
    public String qq;
    public String name;
    public String qlogo;

    @Override
    public String toString() {
        return "QQInfoModel{" +
                "code=" + code +
                ", qq='" + qq + '\'' +
                ", name='" + name + '\'' +
                ", qlogo='" + qlogo + '\'' +
                '}';
    }

}