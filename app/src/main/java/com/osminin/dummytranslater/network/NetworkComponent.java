package com.osminin.dummytranslater.network;

import com.osminin.dummytranslater.network.modules.NetworkModule;
import com.osminin.dummytranslater.presentation.MainPresenterImpl;
import com.osminin.dummytranslater.presentation.TranslationPresenterImpl;
import com.osminin.dummytranslater.presentation.interfaces.TranslationPresenter;

import dagger.Subcomponent;

/**
 * Created by osminin on 3/17/2017.
 */

@NetworkScope
@Subcomponent(modules = NetworkModule.class)
public interface NetworkComponent {
    void inject(TranslationPresenterImpl presenter);
}
