package com.jay.vito.uic.web;

import com.jay.vito.website.core.cache.SystemDataHolder;
import com.jay.vito.website.core.cache.SystemParamKeys;
import com.vito.common.util.validate.Validator;
import com.vito.uic.client.AuthFilter;
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
        this.excludePaths = new String[]{"/login", "/api/login"};
        if (Validator.isNull(this.uicDomain)) {
            this.uicDomain = SystemDataHolder.getParam(SystemParamKeys.UIC_DOMAIN, String.class);
        }
        this.appDomain = this.uicDomain;
    }
}
