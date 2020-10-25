package com.i2pbridge.distribution.common;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.base.insert.InsertMapper;

public interface BaseMapper<T> extends Mapper<T>, IdListMapper<T,Long>, InsertMapper<T> {
}
