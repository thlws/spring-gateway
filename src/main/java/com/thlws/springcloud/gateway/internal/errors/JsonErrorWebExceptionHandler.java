package com.thlws.springcloud.gateway.internal.errors;

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
 * The type Json error web exception handler.
 * @author HanleyTang
 */
public class JsonErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    /**
     * Instantiates a new Json error web exception handler.
     *
     * @param errorAttributes    the error attributes
     * @param resourceProperties the resource properties
     * @param errorProperties    the error properties
     * @param applicationContext the application context
     */
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

    @Override
    @SuppressWarnings("NullableProblems")
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }


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
        return ServerResponse.status(getHttpStatus(request)).contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(error));
    }


//    @Override
//    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
//
//        if (exchange.getResponse().isCommitted() || isDisconnectedClientError(throwable)) {
//            return Mono.error(throwable);
//        }
//        this.errorAttributes.storeErrorInformation(throwable, exchange);
//        ServerRequest request = ServerRequest.create(exchange, this.messageReaders);
//        return getRoutingFunction(this.errorAttributes).route(request).switchIfEmpty(Mono.error(throwable))
//                .flatMap((handler) -> handler.handle(request))
//                .doOnNext((response) -> logError(request, response, throwable))
//                .flatMap((response) -> write(exchange, response));
//
//        return super.handle(exchange, throwable);
//    }
}

