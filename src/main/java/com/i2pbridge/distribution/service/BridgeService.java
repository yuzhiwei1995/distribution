package com.i2pbridge.distribution.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.i2pbridge.distribution.common.R;
import com.i2pbridge.distribution.mapper.BridgeMapper;
import com.i2pbridge.distribution.mapper.DistributionMapper;
import com.i2pbridge.distribution.model.*;
import com.i2pbridge.distribution.utils.IP2CountryUtils;
import com.i2pbridge.distribution.utils.IPUtils;
import com.i2pbridge.distribution.utils.RSAUtils;
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

    @Autowired
    private CertificateService certService;

    @Autowired
    private VoteService voteService;

    private ObjectMapper objectMapper = new ObjectMapper();

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
//        result = bridge.getType() + " " + bridge.getIp() + ":" + bridge.getPort() + " " + bridge.getFingerprint();
        result = getBridgeline(bridge);
        map.put("bridge", result);
        return R.ok().data(map);
    }


    public R getBridgeWithCert(Certificate certificate, HttpServletRequest request) {
        if(certificate == null)
            return R.error().message("没有许可证");

        String certId = certificate.getId();

        Map map = new HashMap(16);

        // 证书生成并发给用户
        if(certService.validCertifate(certificate)){
            certificate = certService.generateCertificate(certificate, request);
        }
        map.put("certificate", certificate);

        return R.ok().data(map).message("获取成功");
    }


    /**
     * I2P系统投票
     * @param certificate
     * @param bridgeId
     * @param vote
     * @return
     */
    public R voteBridge(Certificate certificate, Long bridgeId, Boolean vote) {
        Long userId = certificate.getUser().getId();

        List<Vote> votelist = voteService.selectByUser(userId);

        if(votelist.size() > 0){
            String curTime = df.format(new Timestamp(System.currentTimeMillis()));
            for(Vote v: votelist){
                String pastTime = df.format(v.getVoteTime());
                if(pastTime.equals(curTime)){
                    return R.ok().message("今天已经对网桥投过票了");
                }
            }
        }

        // 投票
        Vote example = new Vote();
        example.setBridgeId(bridgeId);
        example.setVoteTime(new Timestamp(System.currentTimeMillis()));
        example.setUserId(userId);
        example.setVote(vote);
        voteService.insertVote(example);

        return R.ok().message("投票成功");
    }

    /**
     * 获取网桥，并扣除积分
     * @param certificate
     * @return
     */
    public Bridge getBridge(Certificate certificate) {
        List<Bridge> bridges = mapper.selectAll();
        Collections.shuffle(bridges);

        // TODO 用户积分的修改

        return bridges.get(0);
    }


    public String getBridgeline(Bridge bridge) {
        return bridge.getId() + ": " + bridge.getType() + " " + bridge.getIp() + ":" + bridge.getPort() + " "
                + bridge.getCert();
    }
}
