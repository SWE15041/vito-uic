package com.vito.website.core.exception;

import org.springframework.http.HttpStatus;

/**
 * 框架异常对象
 * Created by WUWY on 2016/9/10.
 */
public class HttpException extends RuntimeException {

    private HttpStatus httpStatus;

    private String code;

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, String code) {
        super(message);
        this.code = code;
    }

    public HttpException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public static HttpException of(IErrorCode errorCode) {
        return of(errorCode.getHttpStatus(), errorCode.getCode(),
                errorCode.getMessage(), null);
    }

    public static HttpException of(HttpStatus httpStatus, String code, String message) {
        return of(httpStatus, code, message, null);
    }

    public static HttpException of(HttpStatus httpStatus, String code, String message, Throwable cause) {
        HttpException kmErrorMessage = new HttpException(message, code, cause);
        kmErrorMessage.setHttpStatus(httpStatus);
        return kmErrorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
