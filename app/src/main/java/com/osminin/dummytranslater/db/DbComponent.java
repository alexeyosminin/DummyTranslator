package com.osminin.dummytranslater.db;

import com.osminin.dummytranslater.db.modules.DbModule;
import com.osminin.dummytranslater.network.NetworkComponent;
import com.osminin.dummytranslater.network.modules.NetworkModule;
import com.osminin.dummytranslater.presentation.MainPresenterImpl;
import com.osminin.dummytranslater.presentation.TranslationPresenterImpl;

import dagger.Subcomponent;

/**
 * Created by osminin on 3/21/2017.
 */

@Subcomponent(modules = DbModule.class)
@DbScope
public interface DbComponent {
    NetworkComponent plusNetworkComponent(NetworkModule networkModule);

    void inject(MainPresenterImpl presenter);
}
