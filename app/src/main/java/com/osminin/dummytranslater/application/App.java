package com.osminin.dummytranslater.application;

import android.app.Application;

import com.osminin.dummytranslater.application.DaggerAppComponent;

/**
 * Created by osminin on 3/17/2017.
 */

public final class App extends Application {
    private static AppComponent component;
    public static AppComponent getComponent() {
        return component;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        buildComponent();
    }
    private void buildComponent() {
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

}
