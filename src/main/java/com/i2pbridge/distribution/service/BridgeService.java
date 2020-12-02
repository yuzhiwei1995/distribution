package com.i2pbridge.distribution.service;

import com.i2pbridge.distribution.common.R;
import com.i2pbridge.distribution.mapper.BridgeMapper;
import com.i2pbridge.distribution.mapper.DistributionMapper;
import com.i2pbridge.distribution.model.Bridge;
import com.i2pbridge.distribution.model.BridgeVo;
import com.i2pbridge.distribution.model.Distribution;
import com.i2pbridge.distribution.utils.IP2CountryUtils;
import com.i2pbridge.distribution.utils.IPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class BridgeService {

    @Autowired
    private BridgeMapper mapper;

    @Autowired
    private DistributionMapper distributionMapper;

    private static DateFormat df = new SimpleDateFormat("YYYY-MM-dd");



    public R queryAllBridge() {
        HashMap<String, Object> map = new HashMap();
        List<Bridge> bridges = mapper.selectAll();
        ArrayList<BridgeVo> results = new ArrayList<>();
        for(Bridge bridge : bridges){
            if(bridge.getCountry() == null){
                bridge.setCountry(IP2CountryUtils.ip2Country(bridge.getIp()));
                mapper.updateByPrimaryKey(bridge);
            }
            results.add(BridgeVo.transform(bridge));
        }
        map.put("items", results);
        map.put("total", results.size());
        return R.ok().data(map);
    }

    public R queryOneBridge(HttpServletRequest request) {
        String ip = IPUtils.getIpAddr(request);
        System.out.println(ip);
        Map<String, Object> map = new HashMap<>();

        // 初始化
//        AtomicBoolean mark = new AtomicBoolean(false);
        String result = null;
        Bridge bridge = null;
        boolean mark = false;

        // 查询IP今天是否获取过
        Distribution example = new Distribution();
        example.setIp(ip);

//        List<Distribution> list = distributionMapper.selectByIP(ip);
        List<Distribution> list = distributionMapper.select(example);

        String curTime = df.format(new Timestamp(System.currentTimeMillis()));
        for (Distribution dis : list) {
            if(dis.getDate() != null){
                String pastTime = df.format(dis.getDate());
                if(pastTime.equals(curTime)){
                    mark = true;
                    example = dis;
                    break;
                }
            }
        }

        // 获取一个IP
        if(!mark){
            List<Bridge> bridges = mapper.selectAll();
            Collections.shuffle(bridges);
            bridge = bridges.get(0);
            example.setBridgeId(bridge.getId());
            example.setDate(new Timestamp(System.currentTimeMillis()));
            distributionMapper.insert(example);
        } else {
            bridge = mapper.selectByPrimaryKey(example.getBridgeId());
        }
        result = "obfs4 " + bridge.getIp() + ":" + bridge.getPort() + " " + bridge.getFingerprint();
        map.put("bridge", result);
        return R.ok().data(map);
    }
}
