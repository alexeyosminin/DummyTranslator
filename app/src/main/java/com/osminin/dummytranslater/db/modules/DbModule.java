package com.osminin.dummytranslater.db.modules;

import android.content.Context;

import com.osminin.dummytranslater.db.DbScope;
import com.osminin.dummytranslater.db.interfaces.TranslationDataStore;
import com.osminin.dummytranslater.db.realm.RealmTranslationDataStore;

import dagger.Module;
import dagger.Provides;

/**
 * Created by osminin on 3/21/2017.
 */

@Module
public final class DbModule {

    @Provides
    @DbScope
    public TranslationDataStore provideDataStore(Context context) {
        return new RealmTranslationDataStore(context);
    }
}
