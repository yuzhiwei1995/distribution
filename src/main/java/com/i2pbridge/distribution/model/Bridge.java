package com.i2pbridge.distribution.model;

import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import javax.persistence.Id;
import java.sql.Date;
import java.sql.Timestamp;

@Data
public class Bridge {
    @Id
    private Long id;

    private String ip;

    private Integer port;

    private String fingerprint;

    private String cert;

    private Timestamp addTime;

    private String status;

    private String country;

}
