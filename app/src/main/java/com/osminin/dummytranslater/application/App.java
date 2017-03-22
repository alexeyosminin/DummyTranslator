package com.osminin.dummytranslater.application;

import android.app.Application;

import com.osminin.dummytranslater.db.DbComponent;
import com.osminin.dummytranslater.db.modules.DbModule;
import com.osminin.dummytranslater.network.NetworkComponent;
import com.osminin.dummytranslater.network.modules.NetworkModule;

/**
 * Created by osminin on 3/17/2017.
 */

public final class App extends Application {
    private static AppComponent appComponent;
    private static NetworkComponent networkComponent;
    private static DbComponent dbComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static NetworkComponent plusNetworkComponent() {
        // start lifecycle of network component
        if (networkComponent == null) {
            networkComponent = appComponent.plusNetworkComponent(new NetworkModule());
        }
        return networkComponent;
    }

    public static void clearNetworkComponent() {
        // end lifecycle of network component
        networkComponent = null;
    }

    public static DbComponent plusDbComponent() {
        if (dbComponent == null) {
            dbComponent = appComponent.plusDbComponent(new DbModule());
        }
        return dbComponent;
    }

    public static void clearDbComponent() {
        dbComponent = null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildAppComponent();
    }

    private void buildAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
