package com.pjy.nchu2.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class CommentReplyEntity {
    private int commentId = -1;
    private int stuId ;
    private int toId;
    private String content ;
    private String nickName;
    private String toNickName;
    private String avatar;
    private int likeCount;
    private int status = 0;
    private Timestamp createTime;
    private Timestamp updateTime;
    private boolean read;

}
