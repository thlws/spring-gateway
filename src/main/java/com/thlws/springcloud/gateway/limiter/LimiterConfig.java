package com.thlws.springcloud.gateway.limiter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

/**
 * @author HanleyTang 2020/8/3
 */
@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class LimiterConfig {

    @Min(1)
    private int replenishRate;

    @Min(1)
    private int burstCapacity = 1;

    @Min(1)
    private int requestedTokens = 1;
}
