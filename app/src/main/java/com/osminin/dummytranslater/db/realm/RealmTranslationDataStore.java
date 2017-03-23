package com.osminin.dummytranslater.db.realm;

import android.content.Context;

import com.osminin.dummytranslater.db.interfaces.TranslationDataStore;
import com.osminin.dummytranslater.models.RecentModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by osminin on 3/22/2017.
 */

public final class RealmTranslationDataStore implements TranslationDataStore<RecentModel> {

    private Realm mRealm;

    public RealmTranslationDataStore(Context context) {
        Realm.init(context);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public Observable<RecentModel> add(RecentModel item) {
        return Observable.create(e -> {
            mRealm.beginTransaction();
            RealmRecentModel realmRecentModel = RealmRecentModel.toDbModel(mRealm, item);
            mRealm.commitTransaction();
            e.onNext(realmRecentModel.fromDbModel());
            e.onComplete();
        });
    }

    @Override
    public Observable<RecentModel> update(RecentModel item) {
        //TODO: rework!
        return Observable.just(item);
    }

    @Override
    public Observable<RecentModel> remove(RecentModel item) {
        //TODO: rework!
        return Observable.just(item);
    }

    @Override
    public Single<List<RecentModel>> queryAll() {
        return Observable.fromIterable(mRealm.
                where(RealmRecentModel.class)
                .findAll())
                .subscribeOn(Schedulers.io())
                .map(item -> item.fromDbModel())
                .toList();
    }
}
