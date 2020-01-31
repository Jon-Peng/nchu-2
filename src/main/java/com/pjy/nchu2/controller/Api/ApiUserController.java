package com.pjy.nchu2.controller.Api;

import cn.hutool.core.util.RandomUtil;
import com.pjy.nchu2.entity.UserEntity;
import com.pjy.nchu2.model.user.SignUpModel;
import com.pjy.nchu2.model.user.UserLoginModel;
import com.pjy.nchu2.model.user.SendVerifyModel;
import com.pjy.nchu2.service.AuthService;
import com.pjy.nchu2.service.RedisService;
import com.pjy.nchu2.service.UserService;
import com.pjy.nchu2.utils.JsonResult;
import com.pjy.nchu2.utils.MyMailUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@ResponseBody
@Api(tags = {"User-用户接口"})
public class ApiUserController {

    @Autowired
    private UserService userService;


    @Autowired
    private AuthService authService;

    /**
     * 返回个人资料
     * @param stuId 学号
     * @return
     */
    @ApiOperation("获取用户资料-GET")
    @ResponseBody
    @GetMapping("/api/user/info")
    public JsonResult apiUserInfo(@RequestParam(name = "stuId") int stuId) {
        UserEntity userEntity = userService.getUser(stuId);
//        System.out.println(redisService.set("test","YES!"));
//        System.out.println(redisService.get("admin"));
        return new JsonResult(userEntity);
    }
    @ApiOperation("获取用户资料-POST")
    @ResponseBody
    @PostMapping("/api/user/info")
    public JsonResult apiUserInfoPost(@RequestParam(name = "stuId") int stuId) {
        UserEntity userEntity = userService.getUser(stuId);
        return new JsonResult(userEntity);
    }

    /**
     * 用户登录
     * @param userLoginModel
     * @param request
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("/api/user/login")
    public JsonResult apiUserLogin(@RequestBody UserLoginModel userLoginModel,
                                   HttpServletRequest request) {
        int stuId = userLoginModel.getStuId();
        String password = userLoginModel.getPassword().trim();

        UserEntity user = userService.getUser(stuId);
        if (user != null) {
            if (user.getPassword().equals(password)) {

                String token = authService.createTokenAndSave(user);//创建并保存token
                Map map = new HashMap() {{
                    put("token", token);
                }};
                return new JsonResult(200, "登录成功", map);
            }
            return new JsonResult(500, "密码错误");
        } else {
            return new JsonResult(500, "用户不存在");
        }

    }

    /**
     * 注销 （通过请求头获取用户对象 --token)
     * @param request
     * @return
     */
    @ApiOperation("注销")
    @PostMapping("/api/user/signOut")
    public JsonResult signOut(HttpServletRequest request) {
        UserEntity user = userService.getUser(request);
        //token = request.getHeader(tokenName);
        //redisKey = RedisDefine.K_COMMUNITY_TOKEN_INFO_KEY + token;
        return new JsonResult(200, "注销成功");
    }

    /**
     * 注册
     * @param request
     * @param signUpModel
     * @return
     */
    @ApiOperation("用户注册")
    @PostMapping("/api/user/signUp")
    public JsonResult signUp(HttpServletRequest request,
                             @RequestBody SignUpModel signUpModel){

        String username = signUpModel.getUsername().trim();
        String password = signUpModel.getPassword().trim();
        String email = signUpModel.getEmail().trim();
        String verifivationCode = signUpModel.getVerificationCode().trim();

        if (userService.checkUsernameExists(username)){

            return new JsonResult(500,"用户名已存在！");
        }
        if (!userService.checkVerificationCode(email,verifivationCode)){

            return new JsonResult(500,"验证码错误！");
        }

        UserEntity  user = userService.createUser(username,password,email);//創建用戶

        String token = "createTokenAndSave(user)";//token
        Map resultMap = new HashMap(){{
            put("token",token);
        }};

        return new JsonResult(200,"注冊成功",resultMap);
    }

    /**
     * 发送验证码-邮箱
     * @param sendVerifyModel
     * @return
     */
    @ApiOperation("发送验证码-邮箱")
    @PostMapping("/api/user/signUp/sendVerification")
    public JsonResult sendVerification(@RequestBody SendVerifyModel sendVerifyModel){

        String email = sendVerifyModel.getEmail().trim();
        String code = RandomUtil.randomNumbers(4);
        String content = "欢迎您注册！验证码：" + code +"  祝你生活愉快！";

        userService.saveVerifyCode(email,code);//保存驗證碼至redis
        MyMailUtil.sendMail(email,content);//發送驗證碼
        Map resultMap = new HashMap(){{
            put("verifyCode",code);
        }};
        return new JsonResult(200,"郵件發送成功",resultMap);
    }

}
