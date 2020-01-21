package com.pjy.nchu2.mapper;

import com.pjy.nchu2.entity.CommentReplyEntity;
import com.pjy.nchu2.entity.PostCommentEntity;

import java.util.List;

public interface PostCommentMapper {

    List selectPostComments(int postId);

    void insertComment(PostCommentEntity postCommentEntity);


    //回复

    List<CommentReplyEntity> selectCommentReply(int commentId);

    void insertCommentReply(CommentReplyEntity replyEntity);

    PostCommentEntity selectOneComment(int commentId);

    void updateComment(PostCommentEntity commentEntity);
}
