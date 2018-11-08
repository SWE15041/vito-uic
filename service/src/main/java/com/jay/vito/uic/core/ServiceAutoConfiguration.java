package com.jay.vito.uic.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

/**
 * 作者: zhaixm
 * 日期: 2018/11/8 23:20
 * 描述:
 */
//@Configuration("serviceAutoConfig")
@ConditionalOnProperty(name = "uic.serviceBean.enabled", matchIfMissing = true)
@ComponentScan(basePackages = {"com.jay.vito.uic.service"})
public class ServiceAutoConfiguration {
}
