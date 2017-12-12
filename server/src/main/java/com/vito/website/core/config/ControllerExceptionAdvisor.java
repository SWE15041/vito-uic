package com.vito.website.core.config;

import com.vito.uic.web.vo.ApiErrorResponse;
import com.vito.website.core.exception.HttpException;
import com.vito.website.core.exception.HttpForbiddenException;
import com.vito.website.core.exception.HttpUnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerExceptionAdvisor {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionAdvisor.class);

    @ExceptionHandler(value = {HttpForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrorResponse httpForbiddenException(HttpForbiddenException ex, WebRequest req) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setErrCode(ex.getCode());
        errorResponse.setMsg(ex.getMessage());
        logger.error("无权访问", ex);
        return errorResponse;
    }

    @ExceptionHandler(value = {HttpUnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse httpException(HttpException ex, WebRequest req) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setErrCode(ex.getCode());
        errorResponse.setMsg(ex.getMessage());
        logger.error("认证未通过", ex);
        return errorResponse;
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse unknownException(Exception ex, WebRequest req) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setMsg(ex.getMessage());
        logger.error("未知错误", ex);
        return errorResponse;
    }

}