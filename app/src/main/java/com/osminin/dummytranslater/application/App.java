package com.osminin.dummytranslater.application;

import android.app.Application;

import com.osminin.dummytranslater.BuildConfig;
import com.osminin.dummytranslater.db.DbComponent;
import com.osminin.dummytranslater.db.modules.DbModule;
import com.osminin.dummytranslater.network.NetworkComponent;
import com.osminin.dummytranslater.network.modules.NetworkModule;

import timber.log.Timber;

/**
 * Created by osminin on 3/17/2017.
 */

public final class App extends Application {
    private static AppComponent appComponent;
    private static NetworkComponent networkComponent;
    private static DbComponent dbComponent;
    private static int dbComponentUsers;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static NetworkComponent plusNetworkComponent() {
        Timber.d("plusNetworkComponent: ");
        // start lifecycle of network component
        if (networkComponent == null) {
            networkComponent = dbComponent.plusNetworkComponent(new NetworkModule());
        }
        return networkComponent;
    }

    public static void clearNetworkComponent() {
        Timber.d("clearNetworkComponent: ");
        // end lifecycle of network component
        networkComponent = null;
    }

    public static DbComponent plusDbComponent() {
        Timber.d("plusDbComponent: ");
        if (dbComponent == null) {
            dbComponent = appComponent.plusDbComponent(new DbModule());
        }
        ++dbComponentUsers;
        return dbComponent;
    }

    public static void clearDbComponent() {
        Timber.d("clearDbComponent: ");
        if (dbComponentUsers == 1) {
            dbComponent = null;
        }
        --dbComponentUsers;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildAppComponent();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void buildAppComponent() {
        Timber.d("buildAppComponent");
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
