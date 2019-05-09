package com.pgy.controller;

import com.pgy.model.User;
import com.pgy.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @RequestMapping("/showUser")
    public void selectUser(){
        User user = userService.selectUser(1);
        System.out.println(user);
    }
}
