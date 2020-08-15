package com.thlws.springcloud.gateway.internal.errors;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.thlws.commons.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ServerWebInputException;

import java.util.List;


/**
 * @author hanley
 */
@ControllerAdvice
public class ControllerGlobalErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ApiResult<Void> exceptionHandler(Exception e) {
        logger.error("#exceptionHandler:", e);
        return ApiResult.error();
    }

    @ResponseBody
    @ExceptionHandler(value = {ServerWebInputException.class, HttpMessageNotReadableException.class})
    public ApiResult<Void> serverWebInputException(ServerWebInputException e) {
        logger.error("#serverWebInputException:", e);
        if (e.getRootCause() instanceof InvalidFormatException) {
            InvalidFormatException exception = (InvalidFormatException) e.getRootCause();
            JsonMappingException.Reference reference = exception.getPath().get(0);
            return ApiResult.error(400, "[" + reference.getFieldName() + "] " + exception.getOriginalMessage());
        }
        return ApiResult.error(400, "参数错误");
    }

    @ResponseBody
    @ExceptionHandler(value = WebExchangeBindException.class)
    public ApiResult<Void> webExchangeBindException(WebExchangeBindException e) {
        logger.error("#webExchangeBindException:", e);
        return ApiResult.error(400, e.getFieldError().getField() + ": " + e.getFieldError().getDefaultMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ApiResult<Void> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logger.error("#methodArgumentTypeMismatchException:", e);
        return ApiResult.error(400, "[" + e.getName() + "]" + " 参数类型不匹配");
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult<Void> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("#methodArgumentNotValidException:", e);
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        return getApiResult(errors);
    }

    private ApiResult<Void> getApiResult(List<FieldError> errors) {
        StringBuilder builder = new StringBuilder();
        for (FieldError error : errors) {
            String field = error.getField();
            String message = error.getDefaultMessage();
            builder.append("[").append(field).append("]").append(message).append("; ");
        }

        return ApiResult.error(400, builder.toString());
    }


    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public ApiResult<Void> bindExceptionHandler(BindException e) {
        logger.error("#bindExceptionHandler:", e);

        List<FieldError> errors = e.getFieldErrors();
        return getApiResult(errors);
    }
}
