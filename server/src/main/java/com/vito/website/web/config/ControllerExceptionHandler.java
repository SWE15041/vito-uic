package com.vito.website.web.config;

import com.vito.uic.web.vo.ApiErrorResponse;
import com.vito.website.core.exception.HttpException;
import com.vito.website.core.exception.HttpForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {HttpForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrorResponse httpForbiddenException(HttpForbiddenException ex, WebRequest req) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setErrCode(ex.getCode());
        errorResponse.setMsg(ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(value = {HttpException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse httpException(HttpException ex, WebRequest req) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setErrCode(ex.getCode());
        errorResponse.setMsg(ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse unknownException(Exception ex, WebRequest req) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setMsg(ex.getMessage());
        return errorResponse;
    }

}