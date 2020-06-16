package com.sakura.pmp.server.annotation;

import java.lang.annotation.*;

/**
 * @author : lzl
 * @date : 11:15 2020/5/23
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String value() default "";
}
