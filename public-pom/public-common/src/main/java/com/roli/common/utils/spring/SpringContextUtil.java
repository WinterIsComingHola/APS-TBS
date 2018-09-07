package com.roli.common.utils.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 用于获取spring上下文环境和获取bean的类
 * @author xuxinyu
 * @date 2018/7/17 下午8:36
 */

public class SpringContextUtil implements ApplicationContextAware {

    // Spring应用上下文环境
    private static ApplicationContext applicationContext;


    /*
    * 实现ApplicationContextAware接口的回调方法。设置上下文环境
     * */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取对象
     * **/
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }
}
