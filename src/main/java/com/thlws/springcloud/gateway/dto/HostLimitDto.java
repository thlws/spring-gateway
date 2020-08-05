package com.thlws.springcloud.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 网关限流
 * </p>
 *
 * @author Mybatis plus generator
 * @since 2020-08-04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostLimitDto implements Serializable {

    private Integer limit;

    private String host;

}
