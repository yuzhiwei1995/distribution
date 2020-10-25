package com.i2pbridge.distribution.web.controller;

import com.i2pbridge.distribution.service.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("distribute")
public class DistributionController {
    @Autowired
    private DistributionService service;


}
