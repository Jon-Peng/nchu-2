package com.pjy.nchu2.service;

import com.pjy.nchu2.entity.CommentReplyEntity;
import com.pjy.nchu2.entity.PostCommentEntity;
import com.pjy.nchu2.entity.UserEntity;
import com.pjy.nchu2.mapper.PostCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    PostCommentMapper postCommentMapper;

    public List getPostComments(int postId) {
        return postCommentMapper.selectPostComments(postId);
    }

    public void addComment(HttpServletRequest request,PostCommentEntity postCommentEntity) {
        UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
        postCommentEntity.setAvatar(userEntity.getAvatar());
        postCommentEntity.setNickName(userEntity.getNickName());
        postCommentEntity.setLikeCount(0);
        postCommentEntity.setStuId(userEntity.getStuId());
        postCommentEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        postCommentMapper.insertComment(postCommentEntity);
    }

    public List<CommentReplyEntity> getCommentReply(int commentId) {
        //获取评论下所有回复
        return postCommentMapper.selectCommentReply(commentId);
    }

    public void addCommentReply(CommentReplyEntity replyEntity) {
        postCommentMapper.insertCommentReply(replyEntity);
    }

    public PostCommentEntity getOneComment(int commentId) {
       return postCommentMapper.selectOneComment(commentId);
    }

    public void updateComment(PostCommentEntity commentEntity) {
        postCommentMapper.updateComment(commentEntity);
    }
}
