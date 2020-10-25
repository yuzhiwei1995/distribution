package com.i2pbridge.distribution.service;

import com.i2pbridge.distribution.common.I2PEnum;
import com.i2pbridge.distribution.common.I2PException;
import com.i2pbridge.distribution.common.R;
import com.i2pbridge.distribution.mapper.UserMapper;
import com.i2pbridge.distribution.model.User;
import com.i2pbridge.distribution.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper mapper;

    public User getUserInfo(String username, String password) {
        User user = mapper.selectByUsername(username);
        if(user != null && !password.equals(user.getPassword())){
            throw new I2PException(I2PEnum.LOGIN_FAIL);
        }
        return user;
    }

    public R login(User oUser) {
        // 根据用户名，查询用户信息
        User user = mapper.selectByUsername(oUser.getUsername());

        // 1、是否有用户
        if(user == null) return R.error().data("用户不存在");

        // 2、判断用户名输入是否正确
        if(user.getUsername().equals(oUser.getUsername())){
            // 3、判断密码是否正确，正确则存放token
            if(user.getPassword().equals(oUser.getPassword())){
                Map map = new HashMap(16);
                map.put("token", JwtUtils.getJwtToken(user.getId().toString(),user.getUsername()));
                return R.ok().data(map);
            }
            // 不正确，则提示错误信息
            else return R.error().data("密码错误");
        }

        // 不正确，则提示错误信息
        else  return R.error().data("用户名输入错误");
    }

    public R getInfo(HttpServletRequest request) {
        // 获取jwt解析的信息（用户的id）
        String memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(request);
        // 根据id，查询用户的信息，并将他放入data数据中
        User user = mapper.selectByPrimaryKey(Integer.parseInt(memberIdByJwtToken));
        // 存储用户信息到响应体
        Map map = new HashMap<>();
        map.put("name",user.getUsername());
        map.put("avatar",user.getAvatar());
        return R.ok().data(map);
    }

    public R logout(HttpServletRequest request, HttpServletResponse response) {
//        Cookie[] cookies = request.getCookies();
//        if(cookies!=null){
//            for (Cookie cookie :cookies ) {
//                cookie.setMaxAge(0);
//                cookie.setPath("/");  //路径一定要写上，不然销毁不了
//                response.addCookie(cookie);
//            }
        return R.ok();
//        }
//        else return R.error();

    }
}
