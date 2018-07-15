package com.jay.vito.website.core.exception;

import org.springframework.http.HttpStatus;

/**
 * 异常CODE
 * <p/>
 * Created by WUWY on 2016/9/10.
 */
public interface IErrorCode {

    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();

}
