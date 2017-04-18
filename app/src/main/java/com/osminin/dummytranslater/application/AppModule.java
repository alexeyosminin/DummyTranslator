package com.osminin.dummytranslater.application;

import android.app.Application;
import android.content.Context;

import com.osminin.dummytranslater.presentation.FavoritesPresenterImpl;
import com.osminin.dummytranslater.presentation.MainPresenterImpl;
import com.osminin.dummytranslater.presentation.TranslationPresenterImpl;
import com.osminin.dummytranslater.presentation.interfaces.FavoritesPresenter;
import com.osminin.dummytranslater.presentation.interfaces.MainPresenter;
import com.osminin.dummytranslater.presentation.interfaces.TranslationPresenter;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

/**
 * Created by osminin on 3/17/2017.
 */

@Module
public final class AppModule {

    private final Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    @AppScope
    Context provideContext() {
        return app;
    }

    @Provides
    MainPresenter provideMainPresenter() {
        Timber.d("provideMainPresenter: ");
        return new MainPresenterImpl();
    }

    @Provides
    TranslationPresenter provideTranslationPresenter() {
        Timber.d("provideTranslationPresenter: ");
        return new TranslationPresenterImpl();
    }

    @Provides
    FavoritesPresenter provideFavoritesPresenter() {
        Timber.d("provideFavoritesPresenter: ");
        return new FavoritesPresenterImpl();
    }
}
