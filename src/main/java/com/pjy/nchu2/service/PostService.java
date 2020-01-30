package com.pjy.nchu2.service;

import com.pjy.nchu2.entity.PostEntity;
import com.pjy.nchu2.entity.PostImageEntity;
import com.pjy.nchu2.mapper.PostImageMapper;
import com.pjy.nchu2.mapper.PostMapper;
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
    public PostEntity addTextPost(int postId, int stuId, String title, String tag, String content) {

        PostEntity postEntity = new PostEntity();
        postEntity.setStuId(stuId);
        postEntity.setTitle(title);
        postEntity.setTag(tag);
        postEntity.setContent(content);
        postEntity.setRelease(true);
        postEntity.setPublic(true);
        postEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        if (postId != 0) {

            //update postId不为0 时进行修改操作
            postEntity.setPostId(postId);
            postMapper.update(postEntity);
            System.out.println("@@@@@postEntity@@@@@@@"+postEntity);
            return postEntity;
        } else {

            //postId为0进行添加操作

            postEntity.setCommentCount(0);
            postEntity.setReadCount(0);
            postEntity.setLikeCount(0);
            postEntity.setKeepCount(0);
            postEntity.setType(1);
            postMapper.insert(postEntity);

            return postEntity;
        }

    }
    //修改帖子
    public void updatePost(PostEntity postEntity){
        postMapper.update(postEntity);
    }
    //查询所有帖子
    public List<PostEntity> allPostList() {
        return postMapper.selectAllPost();
    }

    //搜索查询
    public List<PostEntity> allPostList(String keyword) {
        return postMapper.searchPost(keyword);
    }

    public List<PostEntity> allPostList(String keyword, int stuId) {
        return postMapper.searchPostById(
                keyword, stuId);
    }

    /**
     * 添加图片到帖子
     *
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
