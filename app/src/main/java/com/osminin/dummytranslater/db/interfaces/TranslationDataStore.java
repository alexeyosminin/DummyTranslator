package com.osminin.dummytranslater.db.interfaces;

import com.osminin.dummytranslater.models.TranslationModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by osminin on 3/21/2017.
 */

// interface for getting Recent, Suggestions and Favorites
public interface TranslationDataStore {

    Observable init();

    Observable<TranslationModel> add(TranslationModel item);

    Observable<TranslationModel> update(TranslationModel item);

    Observable<TranslationModel> remove(TranslationModel item);

    Observable<TranslationModel> queryAll();
}
