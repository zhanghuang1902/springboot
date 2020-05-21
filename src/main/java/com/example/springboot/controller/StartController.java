package com.example.springboot.controller;

import com.example.springboot.bean.User;
import com.example.springboot.common.JsonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:StartController
 * Package:com.example.springboot
 * Description:
 *
 * @date:2020/2/21 18:33
 * @author:zh
 */
@RestController
@RequestMapping("/start")
public class StartController {


    @RequestMapping("/springboot")
    public String startSpringBoot() {
        return "Welcome to the world of Spring Boot!";
    }

    @RequestMapping("/user")
    public JsonResult<User> getUser() {
        User user = new User(1, "倪升武", "123456");
        return new JsonResult< >(user);
    }

    @RequestMapping("/list")
    public JsonResult<List> getUserList() {
        List<User> userList = new ArrayList<>();
        User user1 = new User(1, "倪升武", "123456");
        User user2 = new User(2, "达人课", "123456");
        userList.add(user1);
        userList.add(user2);
        return new JsonResult<>(userList, "获取用户列表成功");
    }


    @RequestMapping("/map")
    public Map<String, Object> getMap() {
        //todo
        Map<String, Object> map = new HashMap<>(3);
        User user = new User(1, "倪升武", "123456");
        map.put("作者信息", user);
        map.put("博客地址", null);
        map.put("CSDN地址", "http://blog.csdn.net/eson_15");
        map.put("粉丝数量", 4153);
        return map;
    }

}
