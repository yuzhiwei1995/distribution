package com.i2pbridge.distribution.mapper;

import com.i2pbridge.distribution.common.BaseMapper;
import com.i2pbridge.distribution.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where username = #{username}")
    User selectByUsername(@Param("username") String username);
}
