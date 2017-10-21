package com.seuic.app.store.ui.agent;

import com.seuic.app.store.R;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 2017/10/20.
 *
 * @author dpuntu
 */

@Documented
@Target( {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityParams {

    int layoutId() default -1;

    boolean isHome() default true;

    int headBgColor() default R.color.appStoreColor;

    boolean isSearchBarFocusable() default false;

    boolean isRightLayoutShow() default false;
    
    String normalTitle() default "";

    String leftTitle() default "";

    String rightTitle() default "";

    int normalTitleId() default -1;

    int leftTitleId() default -1;

    int rightTitleId() default -1;
}
