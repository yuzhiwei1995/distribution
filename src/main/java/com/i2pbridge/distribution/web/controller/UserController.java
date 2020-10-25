package com.i2pbridge.distribution.web.controller;

import com.i2pbridge.distribution.common.R;
import com.i2pbridge.distribution.model.User;
import com.i2pbridge.distribution.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Api("用户控制器")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService service;

    @CrossOrigin
    @PostMapping("login")
    public R login(@RequestBody User user) {
        System.out.println(user.getUsername());
        return service.login(user);
    }

    @CrossOrigin
    @GetMapping("info")
    public R info(  HttpServletRequest request) {
        return service.getInfo(request);
    }

    @CrossOrigin
    @PostMapping("logout")
    public R logout(HttpServletRequest request, HttpServletResponse response){
        return service.logout(request,response);
    }
}
