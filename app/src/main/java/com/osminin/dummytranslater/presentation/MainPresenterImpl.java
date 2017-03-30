package com.osminin.dummytranslater.presentation;

import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.db.interfaces.TranslationDataStore;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.presentation.interfaces.MainPresenter;
import com.osminin.dummytranslater.ui.MainView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

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
        App.plusDbComponent().inject(this);
    }

    @Override
    public void startObserveUiEvents() {
        verifyDisposable();
        mDisposable.add(mView.textInputObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .map(o -> mTranslationModel)
                .defaultIfEmpty(new TranslationModel())
                .subscribe(mView::showTranslationView));

        mDisposable.add(mView.changeTranslationDirectionClicks()
                .subscribe(mView::changeTransDirection));

        mDisposable.add(mView.fromSpinnerObservable()
                .subscribe(mTranslationModel::setTranslationFrom));

        mDisposable.add(mView.toSpinnerObservable()
                .subscribe(mTranslationModel::setTranslationTo));
    }

    @Override
    public void stopObserveUiEvents() {
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void setTranslationModel(TranslationModel model) {
        mTranslationModel = model;
    }

    private void verifyDisposable() {
        if (mDisposable == null || mDisposable.isDisposed()) {
            mDisposable = new CompositeDisposable();
        }
    }
}
