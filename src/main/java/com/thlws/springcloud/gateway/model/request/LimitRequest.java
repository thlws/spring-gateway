package com.thlws.springcloud.gateway.model.request;

import com.thlws.commons.data.PageRequest;
import com.thlws.springcloud.gateway.model.dto.LimitDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author HanleyTang 2020/8/8
 */
public class LimitRequest extends PageRequest<LimitDto> {

    @Valid
    @NotNull
    private LimitDto data;

    public LimitDto getData() {
        return data;
    }

    public void setData(LimitDto data) {
        this.data = data;
    }
}
