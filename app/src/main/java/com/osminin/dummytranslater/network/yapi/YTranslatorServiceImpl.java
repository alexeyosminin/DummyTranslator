package com.osminin.dummytranslater.network.yapi;

import com.osminin.dummytranslater.BuildConfig;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.network.TranslatorService;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by osminin on 3/17/2017.
 */

public final class YTranslatorServiceImpl implements TranslatorService {

    private YTranslatorService mTranslatorService;

    public YTranslatorServiceImpl(Retrofit retrofit) {
        Timber.d("YTranslatorServiceImpl: ");
        mTranslatorService = retrofit.create(YTranslatorService.class);
    }

    @Override
    public Observable<TranslationModel> translate(TranslationModel model) {
        Timber.d("translate: ");
        String textDirection = model.getTranslationDirection().first.getCode()
                .concat("-")
                .concat(model.getTranslationDirection().second.getCode());
        return mTranslatorService
                .translate(textDirection, BuildConfig.YANDEX_TRANSLATE_API_KEY, model.getPrimaryText())
                .map(responseModel -> responseModel.fromNetworkModel())
                .doOnNext(m -> m.setPrimaryText(model.getPrimaryText()))
                .doOnNext(m -> m.setTranslationDirection(model.getTranslationDirection()))
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io());
    }
}
