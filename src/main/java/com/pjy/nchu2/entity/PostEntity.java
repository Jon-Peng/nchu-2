package com.pjy.nchu2.entity;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class PostEntity {
    private int postId;
    private String title;
    private int commentCount; //
    private int readCount; //
    private int likeCount; //
    private Timestamp createTime;
    private Timestamp updateTime;
    private boolean isRelease; //
    private int keepCount; //
    private int stuId;
    private int type;
    private Timestamp releaseTime;
    private String content;
//    private String htmlContent;
    private String tag;
    private boolean isPublic;
//    private int status;
//    private String voice;
    private String linkUrl;
    private String linkName;
    private String listPhoto;
    private int recommendStatus;
    private Timestamp recommendTime;
    private int isStick;
    private int status = 0; //帖子状态 0

}
