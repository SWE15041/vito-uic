package com.vito.website.web.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.vito.website.core.Application;
import com.vito.website.core.cache.SpringBeanContext;
import com.vito.website.core.cache.SystemDataHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SystemInitListener implements ServletContextListener {

    /**
     * 日志记录器
     */
    private static Logger logger = LoggerFactory.getLogger(SystemInitListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // TODO: 如果有线程，需要在此销毁
        Application.destroy();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        logger.info("系统自定义初始化开始...");
        SpringBeanContext beanContext = new SpringBeanContext(event.getServletContext());
        Application.setBeanContext(beanContext);
        logger.info("系统自定义初始化[OK]");
        otherInit(event);
    }

    public void otherInit(ServletContextEvent event) {
        SystemDataHolder.init();
        SystemDataHolder.setParam(SystemDataHolder.WEBCONTEXT_REAL_PATH, event.getServletContext().getRealPath("/"));
        SystemDataHolder.setParam(SystemDataHolder.WEBCONTEXT_PATH, event.getServletContext().getContextPath());
        // 默认的日期转换格式
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd";
        // 关闭fastjson对循环引用的支持。 如果开启该功能fastjson会使用$ref来链接同一对象，导致其他地方无法正常获取需要的值 -->
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
        // 如果实体属性为enum类型，转换为json时使用enum ordinal而不使用enum name
        JSON.DEFAULT_GENERATE_FEATURE = SerializerFeature.config(JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.WriteEnumUsingName, false);
        // 转换为json时将对象中null值也正常输出
        JSON.DEFAULT_GENERATE_FEATURE = SerializerFeature.config(JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.WriteMapNullValue, true);
    }

}
