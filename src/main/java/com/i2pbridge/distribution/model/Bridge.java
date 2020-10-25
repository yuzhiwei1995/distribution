package com.i2pbridge.distribution.model;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class Bridge {
    @Id
    private Long id;

    private String ip;

    private Integer port;

    private String fingerprint;

    private String cert;

    private Date addTime;

    private Integer status;

}
