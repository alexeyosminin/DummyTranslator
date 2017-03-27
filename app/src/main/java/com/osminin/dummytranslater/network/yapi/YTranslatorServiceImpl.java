package com.osminin.dummytranslater.network.yapi;

import android.util.Log;
import android.util.Pair;

import com.osminin.dummytranslater.models.Languages;
import com.osminin.dummytranslater.models.TranslationModel;
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
    public Observable<TranslationModel> translate(Pair<Languages, Languages> translationDirection, String text) {
        String textDirection = translationDirection.first.getCode()
                .concat("-")
                .concat(translationDirection.second.getCode());
        return mTranslatorService
                .translate(textDirection, API_KEY, text)
                .map(responseModel -> responseModel.fromNetworkModel())
                .doOnNext(model -> model.setPrimaryText(text))
                .doOnNext(model -> model.setTranslationDirection(translationDirection))
                .unsubscribeOn(Schedulers.io());
    }
}
