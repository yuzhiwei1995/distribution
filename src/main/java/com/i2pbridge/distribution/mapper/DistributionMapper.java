package com.i2pbridge.distribution.mapper;


import com.i2pbridge.distribution.common.BaseMapper;
import com.i2pbridge.distribution.model.Distribution;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface DistributionMapper extends BaseMapper<Distribution> {

    @Select("select * from distribution where ip='#{ip}'")
    public List<Distribution> selectByIP(@Param("ip") String id);

}
