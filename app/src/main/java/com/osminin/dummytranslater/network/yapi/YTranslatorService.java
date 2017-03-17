package com.osminin.dummytranslater.network.yapi;

import com.osminin.dummytranslater.network.models.TranslateResponseModel;
import com.osminin.dummytranslater.network.TranslatorService;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by osminin on 3/17/2017.
 */

public interface YTranslatorService {
    @POST("api/v1.5/tr.json/translate")
    Observable<TranslateResponseModel> translate(@Query("lang") String lang,
                                                 @Query("key") String key,
                                                 @Query("text") String text);
}
