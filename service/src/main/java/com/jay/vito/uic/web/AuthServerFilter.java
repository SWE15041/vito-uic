package com.jay.vito.uic.web;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.AuthFilter;
import com.jay.vito.website.core.cache.SystemDataHolder;
import com.jay.vito.website.core.cache.SystemParamKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/**
 * 作者: zhaixm
 * 日期: 2017/11/28 17:07
 * 描述: 认证过滤器
 */
public class AuthServerFilter extends AuthFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthServerFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        addExcludePaths("/login", "/api/login", "/api/userInfo");
        if (Validator.isNull(this.uicDomain)) {
            this.uicDomain = SystemDataHolder.getParam(SystemParamKeys.UIC_DOMAIN, String.class);
        }
        this.appDomain = this.uicDomain;
    }
}
