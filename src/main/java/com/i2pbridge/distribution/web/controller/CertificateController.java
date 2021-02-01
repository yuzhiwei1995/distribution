package com.i2pbridge.distribution.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.i2pbridge.distribution.common.R;
import com.i2pbridge.distribution.model.Certificate;
import com.i2pbridge.distribution.service.CertificateService;
import com.i2pbridge.distribution.utils.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cert")
public class CertificateController {

    @Autowired
    private CertificateService service;

    @CrossOrigin
    @PostMapping("vertify")
    public R vertifyCertificate(@RequestBody Certificate certificate){
        return service.vertify(certificate);
    }

}
