package com.osminin.dummytranslater.network.yapi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by osminin on 3/17/2017.
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Named {
    /** The name. */
    Class value() default Object.class;
}
