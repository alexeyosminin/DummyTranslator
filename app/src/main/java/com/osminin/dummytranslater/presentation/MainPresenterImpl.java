package com.osminin.dummytranslater.presentation;

import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.db.interfaces.TranslationDataStore;
import com.osminin.dummytranslater.models.RecentModel;
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
    public void startObserveTextInput(Observable<Object> observable) {
        verifyDisposable();
        mDisposable.add(observable
                .debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> mView.showTranslationView()));
    }

    @Override
    public void startObserveRecentClicks(Observable<RecentModel> observable) {
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
