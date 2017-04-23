package com.osminin.dummytranslater.presentation;

import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.db.TranslationDataStore;
import com.osminin.dummytranslater.presentation.interfaces.FavoritesPresenter;
import com.osminin.dummytranslater.ui.FavoritesView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by osminin on 4/17/2017.
 */

public final class FavoritesPresenterImpl extends BasePresenterImpl<FavoritesView> implements FavoritesPresenter {

    @Inject
    TranslationDataStore mDataStore;

    @Override
    public void bind(FavoritesView view) {
        super.bind(view);
        App.plusDbComponent().inject(this);
    }

    @Override
    public void startObserveUiEvents() {
        Timber.d("startObserveUiEvents: ");
        prepareDisposable();
        mDisposable.add(mDataStore.open(mDataStore)
                .switchMap(dataStore -> dataStore.queryFavorites())
                .doOnNext(mView::addItem)
                .doOnComplete(() -> mDisposable.add(mDataStore.close(mDataStore).subscribe()))
                .subscribe());
        mDisposable.add(mView.itemClickObservable()
                .subscribe(mView::finishView));
    }

    @Override
    public void stopObserveUiEvents() {
        Timber.d("stopObserveUiEvents: ");
        mView.clearList();
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void destroy() {
        Timber.d("destroy: ");
        App.clearDbComponent();
    }
}
