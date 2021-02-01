package com.i2pbridge.distribution.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.ibatis.annotations.Options;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
public class Distribution {
    @JsonIgnore
    @Id
    private Long id;

    @JsonProperty("bridgeId")
    private Long bridgeId;

    @JsonProperty("bridgeline")
    private transient String bridgeline;

    @JsonIgnore
    private String ip;

    @JsonProperty("distrTime")
    private Timestamp date;

    @JsonIgnore
    private Long userId;

}
