package com.osminin.dummytranslater.network.yapi;

import android.util.Log;

import com.osminin.dummytranslater.network.TranslatorService;
import com.osminin.dummytranslater.network.models.TranslateResponseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.osminin.dummytranslater.Config.API_KEY;

/**
 * Created by osminin on 3/17/2017.
 */

public final class YTranslatorServiceImpl implements TranslatorService {

    private YTranslatorService mTranslatorService;

    public YTranslatorServiceImpl(Retrofit retrofit) {
        mTranslatorService = retrofit.create(YTranslatorService.class);
    }

    @Override
    public Observable<TranslateResponseModel> translate(String lang, String text) {
        return mTranslatorService
                .translate(lang, API_KEY, text)
                .unsubscribeOn(Schedulers.io());
    }
}
