package com.osminin.dummytranslater.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by osminin on 3/21/2017.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface DbScope {
}
