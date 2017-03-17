package com.osminin.dummytranslater.application;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by osminin on 3/17/2017.
 */

@Module
@AppScope
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
}
