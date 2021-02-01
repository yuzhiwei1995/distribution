package com.i2pbridge.distribution;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.i2pbridge.distribution.model.Certificate;
import com.i2pbridge.distribution.service.CertificateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DistributionApplication.class})
public class GenerateCertificateTest {
    @Autowired
    private CertificateService certificateService;

    ObjectMapper mapper = new ObjectMapper();
    @Test
    public void test() throws JsonProcessingException {
        Certificate certificate = certificateService.generateCertificate(null ,null);
        System.out.println(mapper.writeValueAsString(certificate));
    }
}
