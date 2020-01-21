package com.pjy.nchu2.controller;

import com.github.pagehelper.PageHelper;
import com.pjy.nchu2.entity.CommentReplyEntity;
import com.pjy.nchu2.entity.PostCommentEntity;
import com.pjy.nchu2.entity.PostEntity;
import com.pjy.nchu2.entity.UserEntity;
import com.pjy.nchu2.service.CommentService;
import com.pjy.nchu2.service.PostService;
import com.pjy.nchu2.service.UserService;
import com.pjy.nchu2.utils.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommentController {

    @Resource
    CommentService commentService;
    @Resource
    PostService postService;
    @Resource
    UserService userService;

    //评论列表加载
//    @ResponseBody
    @GetMapping("/comment")
    public String getComments(@RequestParam(name = "postId", required = true) int postId,
                              @RequestParam(name = "page", defaultValue = "1") int page,
                              @RequestParam(name = "size", defaultValue = "5") int size,
                              Model model) {

        PageHelper.startPage(page, size);//分页
        System.out.println("帖子id@@@@" + postId);
        List<PostCommentEntity> list = commentService.getPostComments(postId);
        System.out.println("评论列表@@@@@@@@@" + list);
        List<Map> commentList = new ArrayList();

//        map.put("commentList",list);
        for (PostCommentEntity comment : list) {
            Map map = new HashMap();
            map.put("comment", comment);
            List<CommentReplyEntity> replyList = commentService.getCommentReply(comment.getCommentId());
            map.put("replyList", replyList);
            commentList.add(map);
        }
        System.out.println("评论及回复@@@@@@@@" + commentList);
        model.addAttribute("comments", commentList);
        model.addAttribute("postId", postId);
        return "post/postDetail";
//        Map map = new HashMap();
//        map.put("comments",list);
//        return new JsonResult(map);
    }

    //添加评论
    @ResponseBody
    @PostMapping("/comment/add")
    public JsonResult addComment(HttpServletRequest request,
                             @RequestBody PostCommentEntity commentEntity,
                             Model model
                            // @RequestParam(name = "postId") int postId,
                             //@RequestParam(name = "content") String content
                             ) {
        UserEntity userEntity = null;
        userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
        if (userEntity == null) {
            model.addAttribute("error", "哎呀你还没登录嘞ㄟ( ▔, ▔ )ㄏ");
            model.addAttribute("code", 101);

            System.out.println("未登录@@@@@@@@");
            return new JsonResult("error-未登录");
        }
        //int postId = Integer.parseInt(request.getParameter("postId"));
        System.out.println("@@@@@@@@#####"+commentEntity);
        System.out.println("content@@@" + request.getParameter("content"));
        commentService.addComment(request,commentEntity);
        //更新帖子评论数
        PostEntity postEntity = postService.selectOnePost(commentEntity.getPostId());
        postEntity.setCommentCount(postEntity.getCommentCount() + 1);
        postService.updatePost(postEntity);
//        return "redirect:/post/" + postId;
        return new JsonResult("ajax成功！");
    }
    //删除评论

    //添加回复
    @PostMapping("/comment/reply/add")
    public String reply(@RequestParam(name = "commentId") int commentId,
                        @RequestParam(name = "toId") int toId,
                        @RequestParam(name = "content") String content,
                        HttpServletRequest request
    ) {
//        List replyList = commentService.getCommentReply(commentId);
        CommentReplyEntity replyEntity = new CommentReplyEntity();
        UserEntity toUser = userService.getUser(toId);
        UserEntity user = (UserEntity) request.getSession().getAttribute("userEntity");
        replyEntity.setCommentId(commentId);
        replyEntity.setToId(toId);
        replyEntity.setAvatar(user.getAvatar());
        replyEntity.setNickName(user.getNickName());
        replyEntity.setContent(content);
        replyEntity.setStuId(user.getStuId());
        replyEntity.setToNickName(toUser.getNickName());
        replyEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        replyEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        System.out.println("添加回复对象@@@@@@￥￥" + replyEntity);
        commentService.addCommentReply(replyEntity);
        Map postMap = (Map) request.getSession().getAttribute("postMap");
        PostEntity post = (PostEntity) postMap.get("postDetail");
        //更新post评论数（将回复计入评论）
        post.setCommentCount(post.getCommentCount() + 1);
        postService.updatePost(post);
        //更新comment回复数
        PostCommentEntity commentEntity = commentService.getOneComment(commentId);
        commentEntity.setReplyCount(commentEntity.getReplyCount());
        commentService.updateComment(commentEntity);

        return "redirect:/post/" + post.getPostId();
    }
}
