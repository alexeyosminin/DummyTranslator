package com.osminin.dummytranslater.db.realm;

import android.content.Context;

import com.osminin.dummytranslater.db.interfaces.TranslationDataStore;
import com.osminin.dummytranslater.models.TranslationModel;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmQuery;

import static com.osminin.dummytranslater.db.realm.RealmRecentModel.toDbModel;

/**
 * Created by osminin on 3/22/2017.
 */

public final class RealmTranslationDataStore implements TranslationDataStore {

    private Realm mRealm;

    public RealmTranslationDataStore(Context context) {
        Realm.init(context);
    }

    @Override
    public <T> Observable<T> open(T item) {
        return Observable.fromCallable(() -> Realm.getDefaultInstance())
                .doOnNext(realm -> mRealm = realm)
                .switchMap(realm -> Observable.just(item));
    }

    @Override
    public <T> Observable<T> close(T item) {
        return Observable.just(item)
                .doOnNext(i -> mRealm.close());
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
    public Observable<TranslationModel> queryAll() {
        return Observable.fromIterable(mRealm.where(RealmRecentModel.class)
                .findAll())
                .map(m -> m.fromDbModel());
    }

    private void addItem(TranslationModel item) {
        mRealm.beginTransaction();
        RealmRecentModel model = RealmRecentModel.toDbModel(item);
        mRealm.copyToRealmOrUpdate(model);
        mRealm.commitTransaction();
    }
}
