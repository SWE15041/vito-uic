package com.jay.vito.website.core.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

public class MyFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {
    public MyFastJsonHttpMessageConverter() {
        // 在这里配置 fastjson 特性
        // 默认的日期转换格式
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd";
        // 关闭fastjson对循环引用的支持。 如果开启该功能fastjson会使用$ref来链接同一对象，导致其他地方无法正常获取需要的值 -->
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
        // 如果实体属性为enum类型，转换为json时使用enum ordinal而不使用enum name
        JSON.DEFAULT_GENERATE_FEATURE = SerializerFeature.config(JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.WriteEnumUsingName, false);
        // 转换为json时将对象中null值也正常输出
        JSON.DEFAULT_GENERATE_FEATURE = SerializerFeature.config(JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.WriteMapNullValue, true);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return super.supports(clazz);
    }
}