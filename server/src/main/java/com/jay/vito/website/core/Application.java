package com.jay.vito.website.core;


import com.jay.vito.website.core.cache.BeanContext;

/**
 * <pre><b><font color="blue">Application</font></b></pre>
 *
 * <pre><b>&nbsp;--描述说明--</b></pre> <pre></pre> <pre> <b>--样例--</b> Application
 * obj = new Application(); obj.method(); </pre> JDK版本：JDK1.4
 *
 * @author <b>Administrator</b>
 */
public class Application {

    private static BeanContext beanContext;

    /**
     * 重新加载上下文
     *
     * @throws Exception
     */
    public void reload() throws Exception {

    }

    public static BeanContext getBeanContext() {
        return beanContext;
    }

    public static void setBeanContext(BeanContext beanContext) {
        Application.beanContext = beanContext;
    }

    /**
     * 销毁上下文实例
     */
    public static void destroy() {
    }

}
