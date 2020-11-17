package com.i2pbridge.distribution.service;

import com.i2pbridge.distribution.common.R;
import com.i2pbridge.distribution.mapper.BridgeMapper;
import com.i2pbridge.distribution.model.Bridge;
import com.i2pbridge.distribution.model.BridgeVo;
import com.i2pbridge.distribution.utils.IP2CountryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class BridgeService {

    @Autowired
    private BridgeMapper mapper;



    public R queryAllBridge() {
        HashMap<String, Object> map = new HashMap();
        ArrayList<Bridge> bridges = (ArrayList<Bridge>) mapper.selectAll();
        ArrayList<BridgeVo> results = new ArrayList<>();
        for(Bridge bridge : bridges){
            bridge.setCountry(IP2CountryUtils.ip2Country(bridge.getIp()));
            results.add(BridgeVo.transform(bridge));
        }
        map.put("items", results);
        map.put("total", results.size());
        return R.ok().data(map);
    }
}
