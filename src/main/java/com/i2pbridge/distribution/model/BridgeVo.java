package com.i2pbridge.distribution.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Data
public class BridgeVo extends Bridge{
    private String time;
    private static DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
    public static BridgeVo transform(Bridge bridge){
        BridgeVo vo = new BridgeVo();
        vo.setTime(df.format(bridge.getAddTime()));
        vo.setCountry(bridge.getCountry());
        vo.setCert(bridge.getCert());
        vo.setIp(bridge.getIp());
        vo.setId(bridge.getId());
        vo.setFingerprint(bridge.getFingerprint());
        vo.setPort(bridge.getPort());
        vo.setStatus(bridge.getStatus());

        return vo;
    }
}
