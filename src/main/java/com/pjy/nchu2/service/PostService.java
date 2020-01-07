package com.pjy.nchu2.service;

import com.pjy.nchu2.entity.PostEntity;
import com.pjy.nchu2.entity.PostImageEntity;
import com.pjy.nchu2.mapper.PostImageMapper;
import com.pjy.nchu2.mapper.PostMapper;
import com.pjy.nchu2.model.AddTextPostModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service
public class PostService {

    @Resource
    private PostMapper postMapper;

    @Resource
    private PostImageMapper postImageMapper;

    //添加贴子文本信息
    public PostEntity addTextPost(int stuId, String title, String tag,String content) {
        PostEntity postEntity = new PostEntity();
        postEntity.setStuId(stuId);
        postEntity.setTitle(title);
        postEntity.setTag(tag);
        postEntity.setContent(content);
        
        postEntity.setCommentCount(0);
        postEntity.setReadCount(0);
        postEntity.setLikeCount(0);
        postEntity.setRelease(true);
        postEntity.setKeepCount(0);
//        postEntity.setStuId(stuId);
        postEntity.setType(1);
        postEntity.setTag(tag);
//        post.setGameId(-1);
//        post.setHtmlContent("");
        postEntity.setPublic(true);
        postEntity.setReleaseTime(new Timestamp(System.currentTimeMillis()));
        
        postMapper.insert(postEntity);
        
        return postEntity;
    }
    //
    public List<PostEntity> postList(int id){

        return postMapper.selectById(id);
    }
    //查询所有帖子
    public List<PostEntity> allPostList(){
        return postMapper.selectAllPost();
    }
    //搜索查询
    public List<PostEntity> allPostList(String keyword) {
        return postMapper.searchPost(keyword);
    }
    public List<PostEntity> allPostList(String keyword, int stuId) {
        return postMapper.searchPostById(
                keyword,stuId);
    }
    //分页查询帖子
    public List<PostEntity> pagePostList(Integer page, Integer size) {
        Integer start = size*(page-1);
        return postMapper.selectPagePost(start,size);
    }

    /**
     * 添加图片到帖子
     * @param postId     帖子id
     * @param imageUrl   图片地址
     * @param orderIndex 顺序
     */
    public void addPostImage(int postId, String imageUrl, int orderIndex) {
        PostImageEntity image = new PostImageEntity();
        image.setPostId(postId);
        image.setImage(imageUrl);
        image.setOrderIndex(orderIndex);

        postImageMapper.insert(image);

    }


    public List personPostList(int stuId) {
        return postMapper.selectPersonPost(stuId);
    }


    public PostEntity selectOnePost(int postId) {
        return postMapper.selectOnePostById(postId);
    }
}
