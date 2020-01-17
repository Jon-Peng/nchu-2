package com.pjy.nchu2.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pjy.nchu2.entity.PostEntity;
import com.pjy.nchu2.entity.UserEntity;
import com.pjy.nchu2.model.AddTextPostModel;
import com.pjy.nchu2.service.PostService;
import com.pjy.nchu2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PostController {

    @Resource
    private PostService postService;
    @Resource
    private UserService userService;

    @GetMapping("/post/toPublish")
    public String toAdd() {
        return "post/publish";
    }

    //添加文本帖子
    @PostMapping("/post/publish")
    public String addTextPost(@ModelAttribute AddTextPostModel textPostModel,
                              HttpServletRequest request
//                              @RequestParam(value = "postId", required = false) int postId,
                             ) {

        UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
        if (userEntity == null) {
            request.setAttribute("error", "小伙子，没登录就想发帖嘛！");
            return "post/publish";
        }
        int stuId = userEntity.getStuId();
        int postId = 0;
             postId =  textPostModel.getPostId();
        System.out.println(postId);
        String title = textPostModel.getTitle();
        String content = textPostModel.getContent();
        String tag = textPostModel.getTag();

        PostEntity postEntity = postService.addTextPost(postId,stuId, title, tag, content);//写入帖子，返回post
        List imageList = textPostModel.getImageList();
        int orderIndex = 0;
        if (imageList != null && imageList.size() != 0) {
            for (Object image : imageList) {
                postService.addPostImage(postEntity.getPostId(), image.toString(), orderIndex++);
            }
        }
        return "redirect:/";
    }
    //帖子详情
    @GetMapping("/post/{postId}")
    public String postDetail(HttpServletRequest request,
                             @PathVariable(name = "postId") int postId){
        PostEntity postDetail = postService.selectOnePost(postId);
        if (postDetail == null) {
            return "error";
        }
        UserEntity userEntity = userService.getUser(postDetail.getStuId());
        if (userEntity == null) {
            userEntity.setNickName("该账户已注销");
        }
        Map postDetailMap = new HashMap<>();
        postDetailMap.put("postDetail",postDetail);
        postDetailMap.put("postUser",userEntity);
        request.getSession().setAttribute("postMap",postDetailMap);

        postDetail.setReadCount(postDetail.getReadCount()+1);//修改阅读数
        System.out.println("浏览量"+postDetail.getReadCount()+1);
        postService.updatePost(postDetail);
        return "post/postDetail";
    }

    //搜索帖子
    @GetMapping("/post/search")
    public String searchPost(HttpServletRequest request,
                             @RequestParam(name = "page", defaultValue = "1") int page,
                             @RequestParam(name = "size", defaultValue = "5") int size) {

        List<PostEntity> postList = new ArrayList<>();
        String keyword = request.getParameter("keyword");
//        keyword = "%"+keyword+"%";
        String s = request.getParameter("stuId");
//        stuId  = Integer.parseInt();
        PageHelper.startPage(page, size);//分页助手-后面一个查询分页
        if (s == null) {
            postList = postService.allPostList(keyword);
            ////
            PageInfo<PostEntity> pageInfo = new PageInfo<>(postList);//使用pageInfo进行包装
            request.getSession().setAttribute("pageInfo", pageInfo);//存入session
            System.out.println("--搜索PAGE--" + pageInfo);
            postList = pageInfo.getList();
            Map<PostEntity, UserEntity> map = new HashMap<>();//创建帖子:用户 map对想
            for (int i = 0; i < postList.size(); i++) {
                PostEntity post = (PostEntity) postList.get(i);
                UserEntity user = userService.getUser(post.getStuId());
                map.put(post, user);
            }
            System.out.println(map);
            request.getSession().setAttribute("postUserMap", map);
            return "index";
        } else {
            int stuId = Integer.parseInt(s);
            postList = postService.allPostList(keyword,stuId);
            PageInfo<PostEntity> pageInfo = new PageInfo<>(postList);//使用pageInfo进行包装
            request.getSession().setAttribute("pageInfo", pageInfo);//存入session
            System.out.println("--搜索PAGE--" + pageInfo);

            postList = pageInfo.getList();
            Map<PostEntity, UserEntity> map = new HashMap<>();//创建帖子:用户 map对想
            for (int i = 0; i < postList.size(); i++) {
                PostEntity post = (PostEntity) postList.get(i);
                UserEntity user = userService.getUser(post.getStuId());
                map.put(post, user);
            }

            request.getSession().setAttribute("postPersonMap", map);//改为postPersonMap
            return "user/profile";
        }

    }
    //修改帖子
    @GetMapping("/post/editPost")
    public String editPost(HttpServletRequest request,
            Model model){
        Map map = (Map) request.getSession().getAttribute("postMap");
        model.addAttribute("postDetail",map.get("postDetail"));
        return "post/publish";
    }
    //删除帖子
    @GetMapping("/post/deletePost")
    public String deletePost(HttpServletRequest request){
        Map map = (Map) request.getSession().getAttribute("postMap");
        PostEntity postEntity = (PostEntity) map.get("postDetail");
        postEntity.setStatus(-1);
        postService.updatePost(postEntity);
        return "redirect:/user/profile";
    }

}
