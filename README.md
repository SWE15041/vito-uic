# 结构说明

## service
基础依赖包，整合了用户、角色、权限相关的实体、服务、接口等对象。如果项目为单体应用，直接在maven中引入该jar包即可。然后springboot启动类加入`@EnableAuthServer`注解，相关接口就可以自动启用。如果权限相关的控制器需要重写，不想自动注册原有的controller，需要在`application.properties`中增加`uic.controller.enable=false`的配置。

## server

独立的UIC服务，可注册在springcloud中作为独立的认证服务。

## client

认证客户端，需要添加用户登录认证的服务只需要引入client的jar包，并且使用`@EnableAuthClient`注解启用相关拦截器即可。

版本说明
> v2.0.20181120-SNAPSHOT
1. 该版本将springboot升级为2+