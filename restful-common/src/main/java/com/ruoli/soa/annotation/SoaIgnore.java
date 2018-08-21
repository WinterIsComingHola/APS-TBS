package com.ruoli.soa.annotation;

import java.lang.annotation.*;

/**
 * 对于不参与鉴权计算的域成员，需要加上本注解
 * 本注解支持写在类上，如果要在类上使用，需要把将要忽略的域成员名称在
 * 注解上的String数组填充正确，比如：
 * @SoaIgnore({"field1","field2"})
 *
 * 如果写在属性上，请务必不要写任何值
 *
 * @author xuxinyu
 * @Title: SoaIgnore
 * @Package com.ruoli.soa.annotation
 * @Description:
 * @date 2018/2/6 下午7:45
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SoaIgnore {

    String[] value() default {};

}
