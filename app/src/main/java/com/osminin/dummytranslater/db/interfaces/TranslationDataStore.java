package com.osminin.dummytranslater.db.interfaces;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by osminin on 3/21/2017.
 */

// interface for getting Recent, Suggestions and Favorites
public interface TranslationDataStore<T> {

    Observable<T> add(T item);

    Observable<T> update(T item);

    Observable<T> remove(T item);

    Single<List<T>> queryAll();
}
