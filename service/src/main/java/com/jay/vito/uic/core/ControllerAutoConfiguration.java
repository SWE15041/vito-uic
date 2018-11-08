package com.jay.vito.uic.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

/**
 * 作者: zhaixm
 * 日期: 2018/11/8 23:20
 * 描述:
 */
//@Configuration("controllerAutoConfig")
@ConditionalOnProperty(name = "uic.controllerBean.enabled", matchIfMissing = true)
@ComponentScan(basePackages = {"com.jay.vito.uic.web"})
public class ControllerAutoConfiguration {
}
