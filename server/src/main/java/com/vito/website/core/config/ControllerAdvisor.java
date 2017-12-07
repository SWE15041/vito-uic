package com.vito.website.core.config;

import com.vito.website.web.controller.BaseController;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerAdvisor {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAdvisor.class);

    @Before("within(com.vito.website.web.controller.BaseController+)")
    public void before(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        if (target instanceof BaseController) {
            logger.debug("执行BaseController.before()");
            BaseController controller = (BaseController) target;
            controller.before();
        }
    }

    @AfterReturning("within(com.vito.website.web.controller.BaseController+)")
    public void afterReturning(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        if (target instanceof BaseController) {
            logger.debug("执行BaseController.after()");
            BaseController controller = (BaseController) target;
            controller.after();
        }
    }

}