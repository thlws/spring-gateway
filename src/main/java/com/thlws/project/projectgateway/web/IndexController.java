package com.thlws.project.projectgateway.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HanleyTang 2020/6/14
 */
@RestController
public class IndexController {

    @GetMapping
    @RequestMapping("/")
    public String index(){
        return "gateway-"+System.currentTimeMillis();
    }
}
