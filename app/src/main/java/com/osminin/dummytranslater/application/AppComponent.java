package com.osminin.dummytranslater.application;

import com.osminin.dummytranslater.db.DbComponent;
import com.osminin.dummytranslater.db.modules.DbModule;
import com.osminin.dummytranslater.presentation.MainPresenterImpl;
import com.osminin.dummytranslater.presentation.TranslationPresenterImpl;
import com.osminin.dummytranslater.ui.MainActivity;
import com.osminin.dummytranslater.network.NetworkComponent;
import com.osminin.dummytranslater.network.modules.NetworkModule;
import com.osminin.dummytranslater.ui.TranslationActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by osminin on 3/17/2017.
 */

@Component (modules = AppModule.class)
@AppScope
public interface AppComponent {
    DbComponent plusDbComponent(DbModule dbModule);

    void inject(MainActivity activity);
    void inject(TranslationActivity activity);
}
