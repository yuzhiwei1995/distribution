package com.i2pbridge.distribution.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class I2PException extends RuntimeException {
    private I2PEnum i2PEnum;
}
