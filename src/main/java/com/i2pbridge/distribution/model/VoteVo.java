package com.i2pbridge.distribution.model;

import lombok.Data;

@Data
public class VoteVo {

    private Long id;

    private Boolean vote;

    private Long count;
}
