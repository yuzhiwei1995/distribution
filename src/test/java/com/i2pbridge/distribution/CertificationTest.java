package com.i2pbridge.distribution;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.i2pbridge.distribution.model.Bridge;
import com.i2pbridge.distribution.model.Certificate;
import com.i2pbridge.distribution.model.Distribution;
import com.i2pbridge.distribution.model.User;
import com.i2pbridge.distribution.utils.MD5;
import com.i2pbridge.distribution.utils.RSAUtils;
import org.junit.Test;
import sun.security.rsa.RSACore;
import sun.security.rsa.RSAPublicKeyImpl;

import java.security.interfaces.RSAPublicKey;
import java.sql.Timestamp;
import java.util.ArrayList;


public class CertificationTest {
    @Test
    public void certificationJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Certificate certificate = new Certificate();
        // user
        User user = new User();
        user.setId(1L);
        user.setCredit(5L);
        certificate.setUser(user);
        // bridges
        ArrayList<Distribution> list = new ArrayList<>();
        Distribution d = new Distribution();
        d.setBridgeline("obfs4 108.2.5.44:22512 2BBBD91BA796441A3C7BB6D3802083153E17C732");
        d.setBridgeId(1L);
        d.setDate(new Timestamp(System.currentTimeMillis()));
        list.add(d);
        certificate.setBridgelist(list);
        certificate.setIsValid(true);
        certificate.setBuildTime(new Timestamp(System.currentTimeMillis()));
        //String certId = RSAUtils.sign(mapper.writeValueAsString(certificate),"");
        System.out.println(mapper.writeValueAsString(certificate));

        String certId = RSAUtils.sign(mapper.writeValueAsString(certificate));

        certificate.setId(certId);

        System.out.println(certId);
        String s = mapper.writeValueAsString(certificate);


        System.out.println(s.length());
        System.out.println(s);
    }
}
