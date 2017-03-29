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
    public Observable<TranslationModel> translate(TranslationModel model) {
        String textDirection = model.getTranslationDirection().first.getCode()
                .concat("-")
                .concat(model.getTranslationDirection().second.getCode());
        return mTranslatorService
                .translate(textDirection, API_KEY, model.getPrimaryText())
                .map(responseModel -> responseModel.fromNetworkModel())
                .doOnNext(m -> m.setPrimaryText(model.getPrimaryText()))
                .doOnNext(m -> m.setTranslationDirection(model.getTranslationDirection()))
                .unsubscribeOn(Schedulers.io());
    }
}
