package com.i2pbridge.distribution.service;

import com.i2pbridge.distribution.mapper.VoteMapper;
import com.i2pbridge.distribution.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {
    @Autowired
    private VoteMapper mapper;


    public List<Vote> selectByUser(Long userId) {
        Vote vote = new Vote();
        vote.setUserId(userId);
        return mapper.select(vote);
    }

    public void insertVote(Vote example) {
        mapper.insert(example);
    }
}
