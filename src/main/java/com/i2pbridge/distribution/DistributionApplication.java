package com.i2pbridge.distribution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.i2pbridge.distribution.mapper")
public class DistributionApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributionApplication.class, args);
    }

}
