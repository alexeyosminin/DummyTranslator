package com.osminin.dummytranslater.db.realm;

import android.content.Context;

import com.osminin.dummytranslater.db.interfaces.TranslationDataStore;
import com.osminin.dummytranslater.models.TranslationModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by osminin on 3/22/2017.
 */

public final class RealmTranslationDataStore implements TranslationDataStore {

    private Realm mRealm;

    public RealmTranslationDataStore(Context context) {
        Realm.init(context);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public Observable<TranslationModel> add(TranslationModel item) {
        return Observable.just(item)
                .doOnNext(this::addItem);
    }

    @Override
    public Observable<TranslationModel> update(TranslationModel item) {
        //TODO: rework!
        return Observable.just(item);
    }

    @Override
    public Observable<TranslationModel> remove(TranslationModel item) {
        //TODO: rework!
        return Observable.just(item);
    }

    @Override
    public Single<List<TranslationModel>> queryAll() {
        return Observable.fromIterable(mRealm.
                where(RealmRecentModel.class)
                .findAll())
                .subscribeOn(Schedulers.io())
                .map(item -> item.fromDbModel())
                .toList();
    }

    private void addItem(TranslationModel item) {
        mRealm.beginTransaction();
        RealmRecentModel.toDbModel(mRealm, item);
        mRealm.commitTransaction();
    }
}
