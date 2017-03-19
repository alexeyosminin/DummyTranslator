package com.osminin.dummytranslater.application;

import com.osminin.dummytranslater.ui.MainActivity;
import com.osminin.dummytranslater.network.NetworkComponent;
import com.osminin.dummytranslater.network.modules.NetworkModule;
import com.osminin.dummytranslater.ui.TranslationActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by osminin on 3/17/2017.
 */

@Singleton
@Component (modules = AppModule.class)
public interface AppComponent {
    NetworkComponent plusNetworkComponent(NetworkModule networkModule);

    void inject(MainActivity activity);
    void inject(TranslationActivity activity);
}
