package com.osminin.dummytranslater.db.realm;

import android.content.Context;

import com.osminin.dummytranslater.db.TranslationDataStore;
import com.osminin.dummytranslater.models.TranslationModel;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

import static com.osminin.dummytranslater.Config.MAX_RECENTS_COUNT;
import static io.realm.Sort.DESCENDING;

/**
 * Created by osminin on 3/22/2017.
 */

public final class RealmTranslationDataStore implements TranslationDataStore {
    private Realm mRealm;

    public RealmTranslationDataStore(Context context) {
        Timber.d("RealmTranslationDataStore: ");
        Realm.init(context);
    }

    @Override
    public <T> Observable<T> open(T item) {
        return Observable.fromCallable(() -> Realm.getDefaultInstance())
                .doOnNext(realm -> mRealm = realm)
                .doOnNext(realm -> Timber.d("open: "))
                .switchMap(realm -> Observable.just(item));
    }

    @Override
    public <T> Observable<T> close(T item) {
        return Observable.just(item)
                .doOnNext(this::trim)
                .doOnNext(i -> mRealm.close())
                .doOnNext(t -> Timber.d("close: "));
    }

    @Override
    public Observable<TranslationModel> add(TranslationModel item) {
        return Observable.just(item)
                .doOnNext(this::addItem)
                .doOnNext(model -> Timber.d("add: "));
    }

    @Override
    public Observable<TranslationModel> update(TranslationModel item) {
        //TODO: rework (it replaces existing entries with loss of data)
        return add(item);
    }

    @Override
    public Observable<TranslationModel> remove(TranslationModel item) {
        //TODO: rework!
        return Observable.just(item);
    }

    @Override
    public Observable<TranslationModel> queryAll() {
        return Observable.fromIterable(mRealm.where(RealmRecentModel.class)
                .findAllSorted(RealmRecentModel.getSortField(), DESCENDING))
                .doOnNext(m -> Timber.d("queryAll: "))
                .map(m -> m.fromDbModel());
    }

    @Override
    public Observable<TranslationModel> queryFavorites() {
        return Observable.fromIterable(mRealm.where(RealmRecentModel.class)
                .equalTo(RealmRecentModel.getFavoriteField(), true)
                .findAllSorted(RealmRecentModel.getSortField(), DESCENDING))
                .doOnNext(m -> Timber.d("queryFavorites: "))
                .map(m -> m.fromDbModel());
    }

    private void addItem(TranslationModel item) {
        Timber.d("addItem: ");
        mRealm.beginTransaction();
        RealmRecentModel model = RealmRecentModel.toDbModel(item);
        mRealm.copyToRealmOrUpdate(model);
        mRealm.commitTransaction();
    }

    private <T> void trim(T item) {
        Timber.d("trim: ");
        RealmResults<RealmRecentModel> models = mRealm
                .where(RealmRecentModel.class)
                .equalTo(RealmRecentModel.getFavoriteField(), false)
                .findAllSorted(RealmRecentModel.getSortField(), DESCENDING);
        if (models.size() > MAX_RECENTS_COUNT) {
            mRealm.beginTransaction();
            for (int i = MAX_RECENTS_COUNT; i < models.size(); i++) {
                models.deleteFromRealm(i);
            }
            mRealm.commitTransaction();
        }
    }
}
