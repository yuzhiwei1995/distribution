package com.i2pbridge.distribution.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.ArrayList;

@Data
public class Certificate {

    @JsonProperty("certId")
    @Id
    private String id;

    @JsonProperty("user")
    private User user;

    @JsonProperty("bridgelist")
    private ArrayList<Distribution> bridgelist;

    @JsonProperty("buildTime")
    private Timestamp buildTime;

    @JsonProperty("isValid")
    private Boolean isValid;


}
