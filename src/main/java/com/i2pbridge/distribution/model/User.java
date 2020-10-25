package com.i2pbridge.distribution.model;

import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

import javax.persistence.Id;

@Data
public class User {

    @Id
    private Integer id;

    private String roles;

    private String introduction;

    private String avatar;

    private String username;

    private String password;
}
