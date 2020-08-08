package com.thlws.springcloud.gateway.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author HanleyTang 2020/8/8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest<T> implements Serializable {
    private int page;
    private int size;
}
