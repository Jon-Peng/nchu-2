package com.pjy.nchu2.model.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SendVerifyModel {

    @ApiModelProperty("邮箱（验证码）")
    private String email;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }
}
