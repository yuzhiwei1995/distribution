package com.i2pbridge.distribution.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.i2pbridge.distribution.common.I2PEnum;
import com.i2pbridge.distribution.common.I2PException;
import com.i2pbridge.distribution.common.R;
import com.i2pbridge.distribution.mapper.UserMapper;
import com.i2pbridge.distribution.model.Certificate;
import com.i2pbridge.distribution.model.User;
import com.i2pbridge.distribution.utils.JwtUtils;
import com.i2pbridge.distribution.utils.RandomUtil;
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

    @Autowired
    private CertificateService certService;

    private ObjectMapper objectMapper = new ObjectMapper();

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

    public User register() {
        User user = new User();
        user.setCredit(5L);
        mapper.register(user);
        return user;
    }

    public Boolean invitCodeValid(String code){
        // 校验code是否存在
        System.out.println(code);
        User user = mapper.selectByInvitCode(code);
        System.out.println(user);
        return user != null;
    }


    public R invit(String code, HttpServletRequest request) {
        if(!invitCodeValid(code)){
            return R.error().message("邀请码不合法");
        }
        Certificate certificate = certService.generateCertificate(null, request);
        Map map = new HashMap<>();
        map.put("certificate", certificate);
        return R.ok().data(map);
    }

    public R genInvitLink(Certificate certificate) {
        // 查询用户是否存在邀请码
        User user = certificate.getUser();
        user = mapper.selectByPrimaryKey(user);
        if(user.getInvitCode() != null && user.getInvitCode().length() == 10){
            return R.ok().data("http://localhost:8080/user/invit?invit=" + user.getInvitCode());
        }
        // 生成邀请码
        String invitCode = RandomUtil.getRandomString(10);

        // 生成邀请链接 TODO 上线待修改
        String invitLink = "http://localhost:8080/user/invit?invit=" + invitCode;

        // 保存邀请码
        user.setInvitCode(invitCode);
        mapper.updateByPrimaryKey(user);

        return R.ok().data(invitLink);
    }
}
