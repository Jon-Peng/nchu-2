package com.pjy.nchu2.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pjy.nchu2.entity.PostEntity;
import com.pjy.nchu2.entity.UserEntity;
import com.pjy.nchu2.service.PostService;
import com.pjy.nchu2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestController //等价于Controller + ResponseBody
@Controller
public class IndexController {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    @GetMapping("/") //默认访问
    public String index(HttpServletRequest request,
                        @RequestParam(name="page",defaultValue = "1") int page,
                        @RequestParam(name="size",defaultValue = "5") int size
                        ){

        PageHelper.startPage(page,size);//分页助手-后面一个查询分页
        List postList = postService.allPostList();//获取所有帖子列表
        PageInfo<PostEntity> pageInfo = new PageInfo<>(postList);//使用pageInfo进行包装
        request.getSession().setAttribute("pageInfo",pageInfo);//存入session
        System.out.println(pageInfo);
        postList = pageInfo.getList();
        Map<PostEntity, UserEntity> map = new HashMap<>();//创建帖子:用户 map对想
        for(int i=0 ;i<postList.size();i++){
            PostEntity post = (PostEntity) postList.get(i);
            UserEntity user = userService.getUser(post.getStuId());
            map.put(post,user);
        }
        System.out.println(map);
        request.getSession().setAttribute("postUserMap",map);
        return "index";
    }

    //捉小猫
//    @GetMapping("/user/catGame")
//    public String catGame() {
//        return "catGame";
//    }
}
