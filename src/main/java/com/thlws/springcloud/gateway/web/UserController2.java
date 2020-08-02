package com.thlws.springcloud.gateway.web;

import com.thlws.commons.ApiResult;
import com.thlws.springcloud.gateway.dto.UserDto;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author HanleyTang 2020/7/27
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController2 {

    @GetMapping("/info")
    public ApiResult<UserDto> info(){
        UserDto userDto = UserDto.builder().name("hanley")
                .roles(Arrays.asList("admin","super","editor"))
                .introduction("我是Java程序员")
                .avatar("http://www.qq.com/a.png").build();
        return ApiResult.ok(userDto);
    }

    @PostMapping("/logout")
    public ApiResult<Void> logout(){
        return ApiResult.ok();
    }

    @PostMapping("/login")
    public ApiResult<HashMap<String,String>> login() {
        HashMap<String, String> map = new HashMap<>(1);
        map.put("token", UUID.randomUUID().toString());
        return ApiResult.ok(map);
    }
}
