package com.thlws.springcloud.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * @author HanleyTang 2020/7/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String name;
    private List<String> roles = Arrays.asList("admin");
    private String avatar;
    private String introduction = "介绍";

}
