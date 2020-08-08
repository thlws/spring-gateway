package com.thlws.springcloud.gateway.limiter.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author HanleyTang 2020/8/3
 */
@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class LimiterConfig implements Serializable {

    @Min(1)
    private int replenishRate;

    @Min(1)
    private int burstCapacity = 1;

    @Min(1)
    private int requestedTokens = 1;

    @NotEmpty
    private String limitHttpMethod = "POST,PATCH";
}
