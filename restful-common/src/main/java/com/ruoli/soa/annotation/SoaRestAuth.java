package com.ruoli.soa.annotation;

import com.ruoli.soa.model.SoaRestParam;

import java.lang.annotation.*;

/**
 * @author xuxinyu
 * @date 2018/2/9 上午10:37
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SoaRestAuth {

    Class<? extends SoaRestParam> value() default SoaRestParam.class;

}
