package com.i2pbridge.distribution.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Id;

@Data
public class User {

    @JsonProperty("userId")
    @Id
    private Long id;

    @JsonIgnore
    private String roles;

    @JsonIgnore
    private String introduction;

    @JsonIgnore
    private String avatar;

    @JsonIgnore
    private String username;

    @JsonIgnore
    private String password;

    @JsonProperty("credit")
    private Long credit;

    @JsonIgnore
    private String invitCode;
}
