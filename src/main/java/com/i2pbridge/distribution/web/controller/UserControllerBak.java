//package com.i2pbridge.distribution.web.controller;
//
//import com.i2pbridge.distribution.model.User;
//import com.i2pbridge.distribution.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Controller
//@RequestMapping("user")
//public class UserControllerBak {
//
//    @Autowired
//    private UserService service;
//
//    @PostMapping("login")
//    public ResponseEntity<User> login(@RequestParam("username") String username,
//                                      @RequestParam("password") String password,
//                                      HttpServletRequest request){
//        System.out.println(username + " " + password);
//        User user = (User) request.getSession().getAttribute(username);
//        if(user != null){
//            return ResponseEntity.ok(user);
//        }
//        user = service.getUserInfo(username, password);
//        if(user == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        request.getSession().setAttribute(username, user);
//        return ResponseEntity.ok(user);
//    }
//}
