package com.osminin.dummytranslater.db;

import dagger.Subcomponent;

/**
 * Created by osminin on 3/21/2017.
 */

@Subcomponent(modules = DbModule.class)
@DbScope
public interface DbComponent {
}
