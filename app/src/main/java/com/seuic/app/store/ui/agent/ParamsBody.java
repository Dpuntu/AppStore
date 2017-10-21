package com.seuic.app.store.ui.agent;

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
@Target( {ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamsBody {
    String value();
}
