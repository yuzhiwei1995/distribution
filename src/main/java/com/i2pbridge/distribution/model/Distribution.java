package com.i2pbridge.distribution.model;

import lombok.Data;

import javax.persistence.Id;
import java.sql.Timestamp;

@Data
public class Distribution {
    @Id
    private Long id;

    private Long bridgeId;

    private String ip;

    private Timestamp date;

}
