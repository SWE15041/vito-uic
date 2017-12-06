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
        switch (httpStatus) {
            case FORBIDDEN:
                return new HttpForbiddenException(message, code);
            case BAD_REQUEST:
                return new HttpRequestException(message, code);
            case INTERNAL_SERVER_ERROR:
                return new HttpServerException(message, code);
            case UNAUTHORIZED:
                return new HttpUnauthorizedException(message, code);
            default:
                HttpException httpException = new HttpException(message, code, cause);
                httpException.setHttpStatus(httpStatus);
                return httpException;
        }
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
