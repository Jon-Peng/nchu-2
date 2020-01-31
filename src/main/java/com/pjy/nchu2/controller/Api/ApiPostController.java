package com.pjy.nchu2.controller.Api;


import com.pjy.nchu2.service.PostService;
import com.pjy.nchu2.service.UserService;
import com.pjy.nchu2.utils.JsonResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Api(tags = "Post-帖子接口")
public class ApiPostController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @PostMapping("/api/post/list")
    public JsonResult allPost(){
        return null;
    }
}
