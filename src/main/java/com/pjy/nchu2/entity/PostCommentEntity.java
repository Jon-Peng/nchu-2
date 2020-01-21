package com.pjy.nchu2.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PostCommentEntity {
    private int commentId;
    private int postId = -1;
    private int stuId = -1 ;
    private int parentId = -1;// 父评论id (X)
    private String content;
    private Timestamp createTime;
    private Timestamp updateTime;
    private int status;
    private int likeCount;//点赞数
    private int replyCount;//评论回复数
    private String nickName;//昵称
    private String avatar;//头像

    public PostCommentEntity() {

        content = "";
        likeCount = 0;
        status = 0;

    }

}
