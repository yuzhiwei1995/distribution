package com.i2pbridge.distribution.web.controller;

import com.i2pbridge.distribution.common.R;
import com.i2pbridge.distribution.model.Bridge;
import com.i2pbridge.distribution.service.BridgeService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
