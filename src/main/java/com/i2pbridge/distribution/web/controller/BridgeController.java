package com.i2pbridge.distribution.web.controller;

import com.i2pbridge.distribution.common.R;
import com.i2pbridge.distribution.model.Bridge;
import com.i2pbridge.distribution.model.Certificate;
import com.i2pbridge.distribution.model.CertificateVo;
import com.i2pbridge.distribution.service.BridgeService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Data
@RestController
@RequestMapping("bridge")
public class BridgeController {

    @Autowired
    private BridgeService service;

    @CrossOrigin
    @GetMapping("list")
    public R queryAllBridge(@Param("page") int page, @Param("limit") int limit, @Param("sort") String sort){

//        System.out.println(page + limit + sort);
        return service.queryAllBridge();
    }

    @CrossOrigin
    @GetMapping("getBridge")
    public R queryOneBridge(HttpServletRequest request){
        return service.queryOneBridge(request);
    }

    @CrossOrigin
    @PostMapping("getI2PBridge")
    public R getBridgeWithCert(@RequestBody Certificate certificate, HttpServletRequest request){
        System.out.println(certificate);
        return service.getBridgeWithCert(certificate, request);
    }


    @CrossOrigin
    @PostMapping("vote")
    public R voteBridge(@RequestBody CertificateVo certificate){
        return service.voteBridge(certificate.getCertificate(),
                certificate.getBridgeId(), certificate.getVote());
    }

    // TODO 将跨域CrossOrigin去掉
    @CrossOrigin
    @GetMapping("calc")
    public R bridgeCreidtCalc(){
        return service.updateBridgeCredit();
    }

    @CrossOrigin
    @PostMapping("upload")
    public R uploadBridgeline(@RequestParam("bridgeline") String bridgeline,
                              @RequestParam("cert") String certificate,
                              HttpServletRequest request){
        return service.uploadBridge(bridgeline, certificate, request);
    }
}
