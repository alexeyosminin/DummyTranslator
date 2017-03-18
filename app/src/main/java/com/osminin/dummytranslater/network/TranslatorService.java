package com.osminin.dummytranslater.network;

import com.osminin.dummytranslater.network.models.TranslateResponseModel;

import io.reactivex.Observable;

/**
 * Created by osminin on 3/17/2017.
 */

public interface TranslatorService {
    Observable<TranslateResponseModel> translate(String lang, String text);
}
