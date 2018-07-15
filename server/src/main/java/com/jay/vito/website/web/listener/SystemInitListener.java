package com.jay.vito.website.web.listener;

import com.jay.vito.website.core.Application;
import com.jay.vito.website.core.cache.SpringBeanContext;
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
    }

}
