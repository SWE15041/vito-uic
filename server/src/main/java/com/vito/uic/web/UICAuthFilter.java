package com.vito.uic.web;

import com.vito.common.util.validate.Validator;
import com.vito.uic.client.AuthFilter;
import com.vito.website.core.cache.SystemDataHolder;
import com.vito.website.core.cache.SystemParamKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/**
 * 作者: zhaixm
 * 日期: 2017/11/28 17:07
 * 描述: 认证过滤器
 */
public class UICAuthFilter extends AuthFilter {

    private static final Logger logger = LoggerFactory.getLogger(UICAuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        if (Validator.isNull(this.uicDomain)) {
            this.uicDomain = SystemDataHolder.getParam(SystemParamKeys.UIC_DOMAIN, String.class);
        }
        this.appDomain = this.uicDomain;
    }
}