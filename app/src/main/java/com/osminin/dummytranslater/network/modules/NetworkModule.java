package com.osminin.dummytranslater.network.modules;

import com.osminin.dummytranslater.network.TranslatorService;
import com.osminin.dummytranslater.network.yapi.Named;
import com.osminin.dummytranslater.network.yapi.YTranslatorService;
import com.osminin.dummytranslater.network.yapi.YTranslatorServiceImpl;
import com.osminin.dummytranslater.network.NetworkScope;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.osminin.dummytranslater.Config.HOST;

/**
 * Created by osminin on 3/17/2017.
 */

@Module
public final class NetworkModule {
    private static final int REQUESTS_TIMEOUT = 20;
    private static final TimeUnit REQUESTS_TIMEOUT_UNITS = TimeUnit.SECONDS;


    @Provides
    @NetworkScope
    @Named(YTranslatorService.class)
    Retrofit provideRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(REQUESTS_TIMEOUT, REQUESTS_TIMEOUT_UNITS)
                .readTimeout(REQUESTS_TIMEOUT, REQUESTS_TIMEOUT_UNITS)
                .writeTimeout(REQUESTS_TIMEOUT, REQUESTS_TIMEOUT_UNITS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @NetworkScope
    TranslatorService provideTranslatorService(@Named(YTranslatorService.class) Retrofit retrofit) {
        TranslatorService service = new YTranslatorServiceImpl(retrofit);
        return service;
    }
}
