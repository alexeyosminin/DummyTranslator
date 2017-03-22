package com.osminin.dummytranslater.db;

import com.osminin.dummytranslater.db.modules.DbModule;
import com.osminin.dummytranslater.presentation.MainPresenterImpl;

import dagger.Subcomponent;

/**
 * Created by osminin on 3/21/2017.
 */

@Subcomponent(modules = DbModule.class)
@DbScope
public interface DbComponent {
    void inject(MainPresenterImpl presenter);
}
