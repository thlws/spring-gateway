package com.thlws.springcloud.gateway.model.request;

import com.thlws.commons.data.PageRequest;
import com.thlws.springcloud.gateway.model.dto.ApiAuthDto;
import com.thlws.springcloud.gateway.model.dto.LimitDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author HanleyTang 2020/8/8
 */
public class AuthRequest extends PageRequest<LimitDto> {

    @Valid
    @NotNull
    private ApiAuthDto data;

    public ApiAuthDto getData() {
        return data;
    }

    public void setData(ApiAuthDto data) {
        this.data = data;
    }
}
