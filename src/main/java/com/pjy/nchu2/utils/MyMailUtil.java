package com.pjy.nchu2.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;

public class MyMailUtil {

//    private MailAccount account;
    public static void sendMail(String mailTo ,String mailContent){
        MailAccount account = new MailAccount();
        account.setHost("smtp.qq.com");
        account.setPort(25);
        account.setAuth(true);
        account.setFrom("2689659610@qq.com"); //假邮箱，请自己申请真实邮箱
        account.setUser("2689659610@qq.com"); //假邮箱，请自己申请真实邮箱
        account.setPass("bjriurxygmrvddce"); //假密码，请自己申请真实邮箱

        MailUtil.send(account,mailTo, "PJY-测试邮件-驗證碼" + DateUtil.now(), mailContent, false);
    }

    public static void main(String[] args) {
//        Validation
        MyMailUtil.sendMail("tuanmeixlu7@163.com","这是封Java测试邮件 ！！！");
    }
}
