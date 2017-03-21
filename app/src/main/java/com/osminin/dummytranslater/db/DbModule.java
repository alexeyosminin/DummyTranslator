package com.osminin.dummytranslater.db;

import dagger.Module;
import dagger.Provides;

/**
 * Created by osminin on 3/21/2017.
 */

@Module
public final class DbModule {

    @Provides
    @DbScope
    public TranslationDataStore provideDataStore() {
        return null;
    }
}
