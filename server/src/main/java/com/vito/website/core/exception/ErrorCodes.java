package com.vito.website.core.exception;

import org.springframework.http.HttpStatus;

/**
 * 作者: zhaixm
 * 日期: 2017/12/6 15:42
 * 描述: 错误码定义
 */
public enum ErrorCodes implements IErrorCode {

    INVALID_AUTH_TOKEN {
        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.FORBIDDEN;
        }

        @Override
        public String getMessage() {
            return "token验证失败，无权访问该数据";
        }
    },
    INVALID_USERNAME_PASSWORD {
        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.BAD_REQUEST;
        }

        @Override
        public String getMessage() {
            return "用户名或密码错误";
        }
    };

    @Override
    public String getCode() {
        return this.name();
    }

}
