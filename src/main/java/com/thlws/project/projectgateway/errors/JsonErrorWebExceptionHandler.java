package com.thlws.project.projectgateway.errors;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * https://juejin.im/post/5cd66c0a518825506b6c577e
 * @author HanleyTang 2020/7/25
 */
public class JsonErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    public JsonErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                        ResourceProperties resourceProperties,
                                        ErrorProperties errorProperties,
                                        ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }


    private HttpStatus determineHttpStatus(Throwable error, ResponseStatus responseStatus) {
        if (error instanceof ResponseStatusException) {
            return ((ResponseStatusException) error).getStatus();
        }
        if (responseStatus != null) {
            return responseStatus.code();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }


    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Throwable error = super.getError(request);
        Map<String, Object> errorAttributes = new HashMap<>(4);
        errorAttributes.put("message", error.getMessage());
        errorAttributes.put("method", request.methodName());
        errorAttributes.put("path", request.path());
        errorAttributes.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return errorAttributes;
    }

    /***
     * 默认的实现只支持 html ，改造支持所有数据
     * @see DefaultErrorWebExceptionHandler#getRoutingFunction
     * @param errorAttributes
     * @return
     */
    @Override
    @SuppressWarnings("NullableProblems")
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }


    /**
     * 根据 request 解析 http状态码
     * @param request
     * @return
     */
    private HttpStatus getHttpStatus(ServerRequest request) {
        Throwable error = super.getError(request);
        ResponseStatus responseStatus = AnnotatedElementUtils
                .findMergedAnnotation(error.getClass(),ResponseStatus.class);
        return determineHttpStatus(error, responseStatus);
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        boolean includeStackTrace = isIncludeStackTrace(request, MediaType.ALL);
        Map<String, Object> error = getErrorAttributes(request, includeStackTrace);
        return ServerResponse.status(getHttpStatus(request)).contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(error));
    }

}

