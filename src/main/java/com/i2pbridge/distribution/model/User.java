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

    private String username;

    private String password;

    @JsonProperty("credit")
    private Long credit;

    @JsonIgnore
    private String invitCode;

    @JsonIgnore
    public String getUsername() {
        return username;
    }
    @JsonProperty
    public void setUsername(String username) {
        this.username = username;
    }
    @JsonIgnore
    public String getPassword() {
        return password;
    }
    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}
