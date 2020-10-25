package com.i2pbridge.distribution.service;

import com.i2pbridge.distribution.common.R;
import com.i2pbridge.distribution.mapper.BridgeMapper;
import com.i2pbridge.distribution.model.Bridge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class BridgeService {

    @Autowired
    private BridgeMapper mapper;

    public R queryAllBridge() {
        HashMap<String, ArrayList<Bridge>> map = new HashMap();
        ArrayList<Bridge> bridges = (ArrayList<Bridge>) mapper.selectAll();
        map.put("items", bridges);
        return R.ok().data(map);
    }
}
