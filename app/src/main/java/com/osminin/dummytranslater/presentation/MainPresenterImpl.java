package com.osminin.dummytranslater.presentation;

import android.util.Log;

import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.network.NetworkComponent;
import com.osminin.dummytranslater.network.TranslatorService;
import com.osminin.dummytranslater.network.modules.NetworkModule;
import com.osminin.dummytranslater.presentation.interfaces.MainPresenter;
import com.osminin.dummytranslater.ui.MainView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO: Add a class header comment!
 */

public final class MainPresenterImpl implements MainPresenter {

    private MainView mView;
    private Disposable mDisposable;

    @Override
    public void bind(MainView view) {
        mView = view;
    }

    @Override
    public void startObserveTextInput(Observable<Object> observable) {
        mDisposable = observable
                .debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> mView.showTranslationView());
    }

    @Override
    public void stopObserveTextInput() {
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
