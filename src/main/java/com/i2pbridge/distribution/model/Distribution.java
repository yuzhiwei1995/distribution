package com.i2pbridge.distribution.model;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class Distribution {
    @Id
    private Long id;

    private Long bridgeId;

    private String ip;

    private Date date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBridgeId() {
        return bridgeId;
    }

}
