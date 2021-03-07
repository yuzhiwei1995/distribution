package com.i2pbridge.distribution.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.i2pbridge.distribution.common.R;
import com.i2pbridge.distribution.mapper.CertificateMapper;
import com.i2pbridge.distribution.mapper.DistributionMapper;
import com.i2pbridge.distribution.model.Bridge;
import com.i2pbridge.distribution.model.Certificate;
import com.i2pbridge.distribution.model.Distribution;
import com.i2pbridge.distribution.model.User;
import com.i2pbridge.distribution.utils.IPUtils;
import com.i2pbridge.distribution.utils.JwtUtils;
import com.i2pbridge.distribution.utils.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class CertificateService {

    @Autowired
    private UserService userService;

    @Autowired
    private BridgeService bridgeService;

    @Autowired
    private DistributionMapper distMapper;

    @Autowired
    private CertificateMapper certMapper;

    ObjectMapper mapper = new ObjectMapper();

    public R vertify(Certificate certificate) {
        Map map = new HashMap(16);
        if(validCertifate(certificate)) return R.ok().data(map).message("证书合法");

        return R.ok().message("证书验证不通过");
    }

    public Certificate generateCertificate(Certificate certificate, HttpServletRequest request){
        // 证书为空表示新用户
        if(certificate == null) {
            User user = userService.register();
            user.setCredit(5L);
//            System.out.println(user.getId());
            certificate = new Certificate();
            certificate.setUser(user);
        }
        // 获取网桥，并扣除积分
        Bridge bridge = bridgeService.getBridge(certificate);
        User user = userService.getUserByID(certificate.getUser());
        user.setCredit(certificate.getUser().getCredit());
        userService.insertByUser(user);

        if(bridge != null) {
            // 分发
            Distribution d = new Distribution();
            d.setDate(new Timestamp(System.currentTimeMillis()));
            d.setBridgeId(bridge.getId());
            d.setUserId(certificate.getUser().getId());
            d.setIp(IPUtils.getIpAddr(request));
            distMapper.insert(d);
            d.setBridgeline(bridgeService.getBridgeline(bridge));

            ArrayList<Distribution> bridgelist = new ArrayList<>();
            bridgelist.add(d);

            certificate.setBridgelist(bridgelist);

            certificate.setId(null);

            certificate.setBuildTime(new Timestamp(System.currentTimeMillis()));

            certificate.setIsValid(true);
        }

        // 签名
        try {
            String certId = RSAUtils.sign(mapper.writeValueAsString(certificate));
            certificate.setId(certId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return certificate;
    }

    public boolean validCertifate(Certificate certificate){
        String certId = certificate.getId();
        certificate.setId(null);
        try {
            String s = mapper.writeValueAsString(certificate);
            certificate.setId(certId);
            // 公钥验证
            if(certificate.getIsValid() && RSAUtils.vertify(certId, s)){
                return true;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        certMapper.selectByExample()
        return false;
    }
}
