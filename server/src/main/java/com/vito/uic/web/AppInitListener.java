package com.vito.uic.web;

import com.vito.uic.core.cache.AppDataCache;
import com.vito.website.web.listener.SystemInitListener;

import javax.servlet.ServletContextEvent;

/**
 * 作者: zhaixm
 * 日期: 2017/11/29 16:44
 * 描述: 应用初始化监听器
 */
public class AppInitListener extends SystemInitListener {

    @Override
    public void otherInit(ServletContextEvent event) {
        super.otherInit(event);
        AppDataCache.init();
    }
}
