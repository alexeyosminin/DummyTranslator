package com.osminin.dummytranslater.presentation;

import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.db.TranslationDataStore;
import com.osminin.dummytranslater.presentation.interfaces.FavoritesPresenter;
import com.osminin.dummytranslater.ui.FavoritesView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Created by osminin on 4/17/2017.
 */

public final class FavoritesPresenterImpl implements FavoritesPresenter {

    @Inject
    TranslationDataStore mDataStore;
    private FavoritesView mView;
    private CompositeDisposable mDisposable;

    @Override
    public void bind(FavoritesView view) {
        Timber.d("bind: ");
        mView = view;
        App.plusDbComponent().inject(this);
    }

    @Override
    public void startObserveUiEvents() {
        Timber.d("startObserveUiEvents: ");
        verifyDisposable();
        mDisposable.add(mDataStore.open(mDataStore)
                .switchMap(dataStore -> dataStore.queryFavorites())
                .doOnNext(mView::addItem)
                .doOnComplete(() -> mDisposable.add(mDataStore.close(mDataStore).subscribe()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    @Override
    public void stopObserveUiEvents() {
        Timber.d("stopObserveUiEvents: ");
        mView.clearList();
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    private void verifyDisposable() {
        Timber.d("verifyDisposable: ");
        if (mDisposable == null || mDisposable.isDisposed()) {
            mDisposable = new CompositeDisposable();
        }
    }
}
