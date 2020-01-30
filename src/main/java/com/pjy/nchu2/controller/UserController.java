package com.pjy.nchu2.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pjy.nchu2.entity.PostEntity;
import com.pjy.nchu2.entity.UserEntity;
import com.pjy.nchu2.model.user.QQInfoModel;
import com.pjy.nchu2.model.user.UserLoginModel;
import com.pjy.nchu2.service.PostService;
import com.pjy.nchu2.service.UserService;
import com.pjy.nchu2.utils.GetApiDataUtil;
import com.pjy.nchu2.utils.JsonResult;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestController //等价于Controller + ResponseBody
@Controller
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    //登录入口
    @GetMapping("/user/login")
    public String toLogin(@ModelAttribute("userLoginModel") UserLoginModel userLoginModel) {
        return "user/login";
    }

    //登录提交
    @ResponseBody
    @PostMapping(value = "/user/login", consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public JsonResult login(@RequestBody UserLoginModel userLoginModel,
                            HttpServletRequest request,
                            Model model) {

        UserEntity userEntity = userService.login(userLoginModel);//登录获取用户信息

        if (userEntity == null) {
            request.getSession().setAttribute("error", "登录错误");
            return new JsonResult(500, "用户不存在");
        }

        String qq = userEntity.getQQ();//取QQ号
        String qqInfoApi = "https://api.uomg.com/api/qq.info?qq=" + qq;

        String qqInfo = new GetApiDataUtil().getData(qqInfoApi);//调用工具类 获取qq信息【昵称.头像】
        JSONObject jsonObject = JSONObject.fromObject(qqInfo);//转为json对象
        jsonObject.remove("lvzuan");//去除无用信息【绿钻】
        QQInfoModel qim = (QQInfoModel) JSONObject.toBean(jsonObject, QQInfoModel.class);

        request.getSession().setAttribute("QQInfoModel", qim);//save => session

        //判断是否为初始密码
        if (userEntity.getPassword().equals(userEntity.getClassName())) {

            String idNum = userEntity.getIdNum(); //获取身份证号
//                String native_code = idNum.substring(0, 6);//获取身份证前六位
//                idNum = idNum.substring(12);//获取身份证后六位
            userEntity.setAvatar(qim.qlogo);//头像地址
            userEntity.setNickName(qim.getName());//QQ昵称
            userService.firstSet(userEntity);
        }

        String year = userEntity.getIdNum().substring(6, 10);
        String month = userEntity.getIdNum().substring(10, 12);
        String day = userEntity.getIdNum().substring(12, 14);
        Date birthday = new Date(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

        return new JsonResult("登录成功！");
    }

    //退出登录
    @ResponseBody
    @GetMapping("/user/logout")
    public JsonResult logout(HttpServletRequest request) {
        request.getSession().invalidate();//清除session
        System.out.println("@@@退出登录");

        return new JsonResult<>("退出登录");
    }

    //QQ
    @GetMapping("/user/qqLogin")
    public String qqLogin(HttpServletRequest request) {
        String qqLoginApi = "https://api.uomg.com/api/login.qq?method=login&callback=https://www.baidu.com";
        //调用工具类 获取qq信息【昵称.头像】
        String qqInfo = new GetApiDataUtil().getData(qqLoginApi);
        System.out.println("--QQ信息--" + qqInfo);
        JSONObject jsonObject = JSONObject.fromObject(qqInfo);//转为json对象
        System.out.println("--QQ信息--" + jsonObject);
//        jsonObject.remove("lvzuan");//去除无用信息【绿钻】
//        QQInfoModel qim = (QQInfoModel) JSONObject.toBean(jsonObject, QQInfoModel.class);
//        System.out.println("--QQInfoModel--" + qim);
        return null;
    }

    //个人信息
    @GetMapping("/user/info")
    public String userInfo(HttpServletRequest request) {

        return "user/userInfo";
    }

    //个人中心
    @GetMapping("/user/profile")
    public String profile(HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") int page,
                          @RequestParam(name = "size", defaultValue = "5") int size) {
        PageHelper.startPage(page, size);//分页助手-后面一个查询分页
        UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
        List postList = postService.personPostList(userEntity.getStuId());//获取个人帖子列表
        PageInfo<PostEntity> pageInfo = new PageInfo<>(postList);//使用pageInfo进行包装
        request.getSession().setAttribute("pageInfo", pageInfo);//存入session
//        System.out.println(pageInfo);
        postList = pageInfo.getList();
        Map<PostEntity, UserEntity> map = new HashMap<>();//创建帖子:用户 map对想
        for (int i = 0; i < postList.size(); i++) {
            PostEntity post = (PostEntity) postList.get(i);
            UserEntity user = userService.getUser(post.getStuId());
            map.put(post, user);
        }
        System.out.println(map);
        request.getSession().setAttribute("postPersonMap", map);
        return "user/profile";
    }


    //捉小猫
    @GetMapping("/user/catGame")
    public String catGame() {
        return "catGame";
    }
}
