package com.vito.website.core.exception;

/**
 * 作者: zhaixm
 * 日期: 2017/12/2 23:30
 * 描述: 500 status 服务端通用异常
 */
public class HttpServerException extends HttpException {

    public HttpServerException(String message, String code) {
        super(message, code);
    }

}
