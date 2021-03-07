package com.i2pbridge.distribution.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.i2pbridge.distribution.common.R;
import com.i2pbridge.distribution.mapper.BridgeMapper;
import com.i2pbridge.distribution.mapper.DistributionMapper;
import com.i2pbridge.distribution.mapper.VoteMapper;
import com.i2pbridge.distribution.model.*;
import com.i2pbridge.distribution.utils.IP2CountryUtils;
import com.i2pbridge.distribution.utils.IPUtils;
import com.i2pbridge.distribution.utils.RSAUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
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
    private VoteMapper voteMapper;

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
        if(certificate.getUser().getCredit() < 0){
            return R.error().message("积分不够");
        }

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
        List<Bridge> bridges = mapper.selectAccordingUserCredit(certificate.getUser().getCredit());
        Collections.shuffle(bridges);

        // 不为新用户
        if(certificate.getBridgelist() != null)
            // 从网桥中获取的积分
            getCreditFromBridge(certificate);

        // 用户积分的修改
        Bridge b = bridges.get(0);
        User u = certificate.getUser();

        if(u.getCredit() >= b.getCredit()){
            certificate.getUser().setCredit(u.getCredit() - b.getCredit());
        } else {
            return null;
        }

        return b;
    }

    private void getCreditFromBridge(Certificate certificate) {
        Long userC = certificate.getUser().getCredit();
        Long gain = 0L;
        for(Distribution d : certificate.getBridgelist()){
            Bridge bridge = mapper.selectByPrimaryKey(d.getBridgeId());
            long before = d.getDate().getTime();
            long cur = System.currentTimeMillis();
            long blockTime = getBlockTime(d.getBridgeId());
            if(blockTime > before) {
                gain += (Long.min(cur, blockTime) - before) * bridge.getRank();
            }
        }
        // 获取积分
        certificate.getUser().setCredit(userC + gain / 1000 / 3600 / 24);
    }

    private Long getBlockTime(Long bridgeId) {
        Bridge bridge = mapper.selectByPrimaryKey(bridgeId);
        return bridge.getExpireTime() == null ? Long.MAX_VALUE : bridge.getExpireTime().getTime();
    }


    public String getBridgeline(Bridge bridge) {
        return bridge.getId() + ": " + bridge.getType() + " " + bridge.getIp() + ":" + bridge.getPort() + " "
                + bridge.getCert();
    }

    public R updateBridgeCredit() {
        List<VoteVo> votelist = voteMapper.selectAggByBIDAndVote();
        calcBridgeCredit(votelist);
        return R.ok();
    }

    private void calcBridgeCredit(List<VoteVo> votelist) {
        for (int i = 0; i < votelist.size(); i += 2) {
            VoteVo support = votelist.get(i + 1);
            VoteVo reject = votelist.get(i);
            if(support.getCount() + reject.getCount() != 0){
                double p1 = 1.0 * support.getCount() / (support.getCount() + reject.getCount());
                double p2 = 1.0 * reject.getCount() / (support.getCount() + reject.getCount());
                Bridge bridge = mapper.selectByPrimaryKey(support.getId());
                bridge.setCredit(bridge.getCredit() + (long)Math.ceil((p1 - p2) * 3) );
                bridge.setRank((int) Math.ceil(3 * p1));
                mapper.updateByPrimaryKey(bridge);
            }
        }
    }

    // obfs4 216.105.171.26:21513 2BBBD91BA796441A3C7BB6D3802083153E17C732
    public R uploadBridge(String bridgeline, String certificate, HttpServletRequest request) {
        if(bridgeline == null || "".equals(bridgeline)){
            return R.error().message("网桥为空");
        }
        String[] ss = bridgeline.split(" ");
        if(ss.length != 3){
            return R.error().message("网桥格式不正确");
        }
        String[] ipPort = ss[1].split(":");
        if(ipPort.length != 2) return R.error().message("ip地址和端口不正确");
        String ip = ipPort[0];
        if(!validIP(ip)) return R.error().message("IP ping不通");
        int port = Integer.parseInt(ipPort[1]);
        if (port < 0 || port > 65535) return R.error().message("端口不正确");

        Bridge b = new Bridge();
        b.setCredit(5L);
        b.setRank(1);
        b.setAddTime(new Timestamp(System.currentTimeMillis()));
        b.setCert(ss[2]);
        b.setIp(ip);
        b.setPort(port);
        b.setType(ss[0]);
        b.setStatus("draft");
        mapper.insert(b);
        Certificate cert = null;
        if(certificate == null || "".equals(certificate)){
            cert = certService.generateCertificate(null, request);
        }
        Map map = new HashMap<>();
        map.put("certificate", cert);
        return R.ok().message("上传成功").data(map);
    }

    public boolean validIP(String ip) {
        Runtime runtime = Runtime.getRuntime(); // 获取当前程序的运行进对象
        Process process = null; //声明处理类对象
        String line = null; //返回行信息
        InputStream is = null; //输入流
        InputStreamReader isr = null;// 字节流
        BufferedReader br = null;
        boolean res = false;// 结果
        try {
            process = runtime.exec("ping " + ip); // PING

            is = process.getInputStream(); // 实例化输入流
            isr = new InputStreamReader(is);// 把输入流转换成字节流
            br = new BufferedReader(isr);// 从字节中读取文本
            while ((line = br.readLine()) != null) {
                if (line.contains("TTL")) {
                    res = true;
                    break;
                }
            }
            is.close();
            isr.close();
            br.close();
            if (res) {
                System.out.println("ping通  ...");

            } else {
                System.out.println("ping不通...");
            }
        } catch (IOException e) {
            System.out.println(e);
            runtime.exit(1);
        }
        return res;
    }
}
