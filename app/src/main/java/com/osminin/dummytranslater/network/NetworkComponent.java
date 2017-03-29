package com.osminin.dummytranslater.network;

import com.osminin.dummytranslater.network.modules.NetworkModule;
import com.osminin.dummytranslater.presentation.MainPresenterImpl;
import com.osminin.dummytranslater.presentation.TranslationPresenterImpl;

import dagger.Subcomponent;

/**
 * Created by osminin on 3/17/2017.
 */

@Subcomponent(modules = NetworkModule.class)
@NetworkScope
public interface NetworkComponent {
    void inject(TranslationPresenterImpl presenter);

    void inject(MainPresenterImpl presenter);
}
