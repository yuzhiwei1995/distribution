package com.i2pbridge.distribution.mapper;

import com.i2pbridge.distribution.common.BaseMapper;
import com.i2pbridge.distribution.model.Vote;
import com.i2pbridge.distribution.model.VoteVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface VoteMapper extends BaseMapper<Vote> {

    @Select("SELECT bridge_id AS id, vote, COUNT(*) AS COUNT FROM vote \n" +
            "WHERE UNIX_TIMESTAMP(vote_time) > UNIX_TIMESTAMP(DATE_ADD(DATE(CURDATE()),INTERVAL -1 DAY))\n" +
            "GROUP BY bridge_id, vote ORDER BY bridge_id, vote")
    List<VoteVo> selectAggByBIDAndVote();
}
