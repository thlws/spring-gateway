package com.thlws.springcloud.gateway.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HanleyTang 2020/7/27
 */
@RestController
public class IndexController {

    @Value("${db.username}")
    private String username;

    @RequestMapping("/db")
    public String index(){
        return username;
    }
}
