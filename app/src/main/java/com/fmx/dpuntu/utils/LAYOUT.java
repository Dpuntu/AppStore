package com.fmx.dpuntu.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
//@Target(METHOD)
//@Retention(RUNTIME)
public @interface LAYOUT {
    int contentView() default -1;
}
