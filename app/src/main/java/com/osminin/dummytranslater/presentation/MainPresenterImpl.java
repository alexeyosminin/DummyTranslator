package com.osminin.dummytranslater.presentation;

import android.util.Log;

import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.db.interfaces.TranslationDataStore;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.presentation.interfaces.MainPresenter;
import com.osminin.dummytranslater.ui.MainView;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO: Add a class header comment!
 */

public final class MainPresenterImpl implements MainPresenter {

    @Inject
    TranslationDataStore mDataStore;

    private MainView mView;
    private TranslationModel mTranslationModel;
    private CompositeDisposable mDisposable;

    @Override
    public void bind(MainView view) {
        mView = view;
        mTranslationModel = new TranslationModel();
        App.plusDbComponent().inject(this);
    }

    @Override
    public void startObserveUiEvents() {
        verifyDisposable();
        mDisposable.add(mView.textInputObservable()
                .map(o -> mTranslationModel)
                .switchMap(mView::showTranslationView)
                .subscribe());

        mDisposable.add(mView.changeTranslationDirectionClicks()
                .switchMap(mView::changeTransDirection)
                .subscribe());

        mDisposable.add(mView.fromSpinnerObservable()
                .subscribe(l -> mTranslationModel.setTranslationFrom(l)));

        mDisposable.add(mView.toSpinnerObservable()
                .subscribe(l -> mTranslationModel.setTranslationTo(l)));

        mDisposable.add(mView.onActivityResult()
                .doOnNext(model -> mTranslationModel = model)
                .switchMap(mView::setPrimaryText)
                .filter(m -> m.getTranslations() != null
                        && !m.getTranslations().isEmpty())
                .switchMap(mView::setTranslationText)
                .doOnError(m -> mView.showError())
                .subscribe());
        mDisposable.add(loadRecent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .switchMap(mView::addRecentItem)
                .subscribe());
    }

    @Override
    public void stopObserveUiEvents() {
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    private Observable<TranslationModel> loadRecent() {
        return mDataStore.open(mDataStore)
                .switchMap(dataStore -> dataStore.queryAll())
                .doOnComplete(() -> mDisposable.add(mDataStore.close(mDataStore).subscribe()))
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void verifyDisposable() {
        if (mDisposable == null || mDisposable.isDisposed()) {
            mDisposable = new CompositeDisposable();
        }
    }
}
