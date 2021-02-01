package com.i2pbridge.distribution.model;

import lombok.Data;

import javax.persistence.Id;
import java.sql.Timestamp;

@Data
public class Vote {
    @Id
    private Long id;

    private Long userId;

    private Long bridgeId;

    private Timestamp voteTime;

    private Boolean vote;
}
