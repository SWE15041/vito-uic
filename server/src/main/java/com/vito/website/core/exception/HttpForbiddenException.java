package com.vito.website.core.exception;

/**
 * 作者: zhaixm
 * 日期: 2017/12/2 23:30
 * 描述: 403 forbidden status
 */
public class HttpForbiddenException extends HttpException {

    public HttpForbiddenException(String message, String code) {
        super(message, code);
    }

}
