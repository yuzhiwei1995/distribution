package com.i2pbridge.distribution.mapper;

import com.i2pbridge.distribution.common.BaseMapper;
import com.i2pbridge.distribution.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where username = #{username}")
    User selectByUsername(@Param("username") String username);

    @Insert("insert into user(credit) values(5)")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int register(User user);

    @Select("select * from user where invit_code = #{code}")
    User selectByInvitCode(@Param("code") String code);

    @Select("select avg(credit) from user")
    double getCreditAvg();
}
