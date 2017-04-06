package com.osminin.dummytranslater.db.realm;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.realm.RealmModel;

/**
 * Created by osminin on 4/6/2017.
 */

public class RealmObjectObservable<T extends RealmModel> implements
        ObservableOnSubscribe<T> {

    private final T object;

    private RealmObjectObservable(@NonNull T object) {
        this.object = object;
    }

    public static <T extends RealmModel> Observable<T> from(@NonNull T object) {
        return Observable.create(new RealmObjectObservable<T>(object));
    }

    @Override
    public void subscribe(ObservableEmitter<T> emitter) throws Exception {
        emitter.onNext(object);
    }
}
