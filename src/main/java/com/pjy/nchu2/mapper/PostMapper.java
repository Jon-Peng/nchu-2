package com.pjy.nchu2.mapper;

import com.pjy.nchu2.entity.PostEntity;
import com.pjy.nchu2.model.AddTextPostModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {

    void insert(PostEntity post);
    void delete(PostEntity post);
    void update(PostEntity post);

    //查询帖子
    List<PostEntity> selectById(int id);
    List<PostEntity> selectAllPost();//查询所有
    List<PostEntity> selectPagePost(Integer start, Integer size);//手动分页查询
    List selectPersonPost(int stuId);//查询个人帖子
    PostEntity selectOnePostById(int postId);//ID查询贴子

    //搜索查询
    List<PostEntity> searchPost(String keyword);

    List<PostEntity> searchPostById(String keyword, int stuId);
}
