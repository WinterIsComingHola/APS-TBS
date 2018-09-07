package com.ruoli.soa.annotation;

import java.lang.annotation.*;

/**
 * @author xuxinyu
 * @date 2018/8/29 上午10:37
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MqHander {
    String value();
}
