package com.i2pbridge.distribution.mapper;

import com.i2pbridge.distribution.common.BaseMapper;
import com.i2pbridge.distribution.model.Bridge;
import com.i2pbridge.distribution.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BridgeMapper extends BaseMapper<Bridge> {

    @Select("select * from bridge where credit <= #{credit}")
    List<Bridge> selectAccordingUserCredit(@Param("credit") Long credit);
}
