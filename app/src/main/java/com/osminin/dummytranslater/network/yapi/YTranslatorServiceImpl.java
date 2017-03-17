package com.osminin.dummytranslater.network.yapi;

import com.osminin.dummytranslater.network.TranslatorService;
import com.osminin.dummytranslater.network.models.TranslateResponseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Created by osminin on 3/17/2017.
 */

public final class YTranslatorServiceImpl implements TranslatorService {

    private YTranslatorService mTranslatorService;

    public YTranslatorServiceImpl(Retrofit retrofit) {
        mTranslatorService = retrofit.create(YTranslatorService.class);
    }

    @Override
    public Observable<TranslateResponseModel> translate(String lang, String key, String text) {
        return mTranslatorService.translate(lang, key, text);
    }
}
