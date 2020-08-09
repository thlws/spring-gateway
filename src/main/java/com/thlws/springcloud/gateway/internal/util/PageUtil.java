package com.thlws.springcloud.gateway.internal.util;

import com.thlws.commons.data.PageRequest;

import java.util.Objects;

/**
 * @author HanleyTang 2020/8/9
 */
public class PageUtil {

    public static void build(PageRequest request) {
        if (Objects.isNull(request.getPage())) {
            request.setPage(1);
        }
        if (Objects.isNull(request.getSize())) {
            request.setSize(20);
        }
    }
}
