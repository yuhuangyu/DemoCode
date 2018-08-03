package com.api.utils.annotationprocessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fj on 2018/8/2.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface CompileAnnotation {
    int value() default 0;
}
