package com.osminin.dummytranslater.presentation;

import com.jakewharton.rxbinding2.internal.Notification;
import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.db.interfaces.TranslationDataStore;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.presentation.interfaces.MainPresenter;
import com.osminin.dummytranslater.ui.MainView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * TODO: Add a class header comment!
 */

public final class MainPresenterImpl implements MainPresenter {

    @Inject
    TranslationDataStore mDataStore;

    private MainView mView;
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
                .subscribe(o -> mView.showTranslationView()));
    }

    @Override
    public void startObserveRecentClicks(Observable<TranslationModel> observable) {
        verifyDisposable();
        mDisposable.add(observable.subscribe());
    }

    @Override
    public void stopObserveUiEvents() {
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    private void verifyDisposable() {
        if (mDisposable == null || mDisposable.isDisposed()) {
            mDisposable = new CompositeDisposable();
        }
    }
}
