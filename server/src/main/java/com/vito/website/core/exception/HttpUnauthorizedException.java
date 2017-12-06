package com.vito.website.core.exception;

/**
 * 作者: zhaixm
 * 日期: 2017/12/2 23:30
 * 描述: 400 status  通用请求参数错误相关的异常
 */
public class HttpUnauthorizedException extends HttpException {

    public HttpUnauthorizedException(String message, String code) {
        super(message, code);
    }

}
