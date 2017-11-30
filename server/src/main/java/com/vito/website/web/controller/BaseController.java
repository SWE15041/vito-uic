/**
 * 文件名: BaseController.java
 * 作者: zhaixm
 * 版本: 2014-7-1 下午5:07:44 v1.0
 * 日期: 2014-7-1
 * 描述:
 */
package com.vito.website.web.controller;

import com.vito.common.util.web.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 作者: zhaixm
 * 日期: 2014-7-1 下午5:07:44
 * 描述:
 */
public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    protected HttpServletRequest getRequest() {
        return request;
    }

    protected HttpServletResponse getResponse() {
        return response;
    }

    public void before() {
//		logger.debug("方法前拦截");
        // 会在所有controller方法执行前调用该方法，实现方式spring aop ControllerAdvice
    }

    public void after() {
//		logger.debug("方法后拦截");
        // 会在所有controller方法执行后调用该方法
    }

    protected Map<String, Object> parseParams() {
        return WebUtil.parseParams(request);
    }

}
