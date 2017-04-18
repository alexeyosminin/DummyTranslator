package com.osminin.dummytranslater.db.modules;

import android.content.Context;

import com.osminin.dummytranslater.db.DbScope;
import com.osminin.dummytranslater.db.TranslationDataStore;
import com.osminin.dummytranslater.db.realm.RealmTranslationDataStore;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

/**
 * Created by osminin on 3/21/2017.
 */

@Module
public final class DbModule {

    @Provides
    @DbScope
    public TranslationDataStore provideDataStore(Context context) {
        Timber.d("provideDataStore: ");
        return new RealmTranslationDataStore(context);
    }
}
