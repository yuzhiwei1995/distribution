package com.i2pbridge.distribution.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum  I2PEnum {
    LOGIN_FAIL(400, "用户名和密码不正确")
    ;
    private int code;
    private String msg;
}
