package com.pjy.nchu2.controller;


import com.fasterxml.jackson.databind.util.JSONPObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pjy.nchu2.entity.PostEntity;
import com.pjy.nchu2.entity.UserEntity;
import com.pjy.nchu2.model.QQInfoModel;
import com.pjy.nchu2.model.UserLoginModel;
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
import java.time.LocalDateTime;
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
    @PostMapping(value = "/user/login",consumes="application/json",headers = "content-type=application/x-www-form-urlencoded")
    public String login(@ModelAttribute UserLoginModel userLoginModel,
                        HttpServletRequest request,
                        Model model) {
        //登录获取用户信息
        UserEntity userEntity = userService.login(userLoginModel);
        System.out.println("---request---" + request.getParameter("stuId"));
        if (userEntity == null) {
            request.getSession().setAttribute("error","登录错误");
            return "user/login";
        } else {
            System.out.println("--登录返回model--"+userLoginModel);
            //取QQ号
            String qq = userEntity.getQQ();
            String qqInfoApi = "https://api.uomg.com/api/qq.info?qq=" + qq;
            //调用工具类 获取qq信息【昵称.头像】
            String qqInfo = new GetApiDataUtil().getData(qqInfoApi);
            System.out.println("--QQ信息--" + qqInfo);
            JSONObject jsonObject = JSONObject.fromObject(qqInfo);//转为json对象
            jsonObject.remove("lvzuan");//去除无用信息【绿钻】
            QQInfoModel qim = (QQInfoModel) JSONObject.toBean(jsonObject, QQInfoModel.class);
            System.out.println("--QQInfoModel--" + qim);
            //save
            request.getSession().setAttribute("QQInfoModel", qim);
            //判断是否为初始密码
            if (userEntity.getPassword().equals(userEntity.getClassName())) {
                //获取身份证号
                String idNum = userEntity.getIdNum();
//                String native_code = idNum.substring(0, 6);//获取身份证前六位
                idNum = idNum.substring(12);//获取身份证后六位

//                userEntity.setPassword(idNum);
//                userEntity.setNative_code(native_code);
//                System.out.println(userEntity);

//                userService.setPassword(userEntity);//设为密码
//                userService.setNativeCode(userEntity);//设置native_code
                userEntity.setAvatar(qim.qlogo);//头像地址
                userEntity.setNickName(qim.getName());
                userService.firstSet(userEntity);
            }
            String year = userEntity.getIdNum().substring(6,10);
            String month = userEntity.getIdNum().substring(10,12);
            String day = userEntity.getIdNum().substring(12,14);
            Date birthday = new Date(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
            LocalDateTime birthday2 =  LocalDateTime.now();
            long old =birthday.getTime();
            long now = new Date().getTime();
            int days = (int) ((now-old)/(24*60*60*1000));

            System.out.println("--old--"+old+"--now--"+now+"--days--"+days);
            System.out.println("--year--"+year+"--month--"+month+"--day--"+day);
            request.getSession().setAttribute("liveDays",days);
            request.getSession().setAttribute("userEntity", userEntity);//将用户存入session

            return "redirect:/";//重定向至其他controller
        }
//        return null;
    }
    //退出登录
    @GetMapping("/user/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }
    //QQ
    @GetMapping("/user/qqLogin")
    public String qqLogin(HttpServletRequest request){
        String qqLoginApi = "https://api.uomg.com/api/login.qq?method=login&callback=https://www.baidu.com";
        //调用工具类 获取qq信息【昵称.头像】
        String qqInfo = new GetApiDataUtil().getData(qqLoginApi);
        System.out.println("--QQ信息--" + qqInfo);
        JSONObject jsonObject = JSONObject.fromObject(qqInfo);//转为json对象
        System.out.println("--QQ信息--"+jsonObject);
//        jsonObject.remove("lvzuan");//去除无用信息【绿钻】
//        QQInfoModel qim = (QQInfoModel) JSONObject.toBean(jsonObject, QQInfoModel.class);
//        System.out.println("--QQInfoModel--" + qim);
        return null;
    }

    //个人信息
    @GetMapping("/user/info")
    public String userInfo(HttpServletRequest request){

        return "user/userInfo";
    }

    //个人中心
    @GetMapping("/user/profile")
    public String profile(HttpServletRequest request,
                          @RequestParam(name="page",defaultValue = "1") int page,
                          @RequestParam(name="size",defaultValue = "5") int size){
        PageHelper.startPage(page,size);//分页助手-后面一个查询分页
        UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
        List postList = postService.personPostList(userEntity.getStuId());//获取个人帖子列表
        PageInfo<PostEntity> pageInfo = new PageInfo<>(postList);//使用pageInfo进行包装
        request.getSession().setAttribute("pageInfo",pageInfo);//存入session
//        System.out.println(pageInfo);
        postList = pageInfo.getList();
        Map<PostEntity, UserEntity> map = new HashMap<>();//创建帖子:用户 map对想
        for(int i=0 ;i<postList.size();i++){
            PostEntity post = (PostEntity) postList.get(i);
            UserEntity user = userService.getUser(post.getStuId());
            map.put(post,user);
        }
        System.out.println(map);
        request.getSession().setAttribute("postPersonMap",map);
        return "user/profile";
    }

    //api
    @ResponseBody
    @GetMapping("/api/user/info")
    public JsonResult apiUserInfo(@RequestParam(name = "stuId") int stuid){
        UserEntity userEntity = userService.getUser(stuid);
        return new JsonResult(userEntity);
    }
    //捉小猫
    @GetMapping("/user/catGame")
    public String catGame() {
        return "catGame";
    }
}
