package com.rxl.controller;

import com.rxl.entity.UserEntity;
import com.rxl.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ren.xiaolong
 * @date 2022/4/18
 * @Description
 */
@RestController
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping("/register")
    public String register(){
        UserEntity userEntity=new UserEntity();
        userEntity.setId(4);
        userEntity.setUsername("zhang");
        userEntity.setPassword("123");
        userService.register(userEntity);
        return "注册成功";

    }

    /**
     * 使用注解来限定接口的权限
     *  @RequiresRoles  需要具备什么角色 才可以访问到这个接口
     *  @RequiresPermissions  需要具备什么操作权限才可以访问到这个接口
     *  和shiroConfig 中的ShiroFilterFactoryBean 资源过滤可以进行设置哪些接口路径需要套认证
     * @return
     */
    @RequiresRoles(value={"admin"})//用来判断角色  同时具有 admin
    @RequiresPermissions("user:update:013") //用来判断权限字符串
    @GetMapping("/test")
    public String test(){
        return "true";
    }


    @GetMapping("/login")
    public UserEntity login(@RequestParam String userName,@RequestParam String password){
        UserEntity login = userService.login(userName, password);
        return login;

    }
}
