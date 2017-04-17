package com.osminin.dummytranslater.db;

import com.osminin.dummytranslater.models.TranslationModel;

import io.reactivex.Observable;

/**
 * Created by osminin on 3/21/2017.
 */

// interface for getting Recent, Suggestions and Favorites
public interface TranslationDataStore {

    <T> Observable<T> open(T item);

    <T> Observable<T> close(T item);

    Observable<TranslationModel> add(TranslationModel item);

    Observable<TranslationModel> update(TranslationModel item);

    Observable<TranslationModel> remove(TranslationModel item);

    Observable<TranslationModel> queryAll();

    Observable<TranslationModel> queryFavorites();
}
