package com.osminin.dummytranslater.db.interfaces;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by osminin on 3/21/2017.
 */

// interface for getting Recent, Suggestions and Favorites
public interface TranslationDataStore<T> {

    void add(T item);

    void update(T item);

    void remove(T item);

    Single<List<T>> queryAll();
}
