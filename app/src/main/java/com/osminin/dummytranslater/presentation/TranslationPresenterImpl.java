package com.osminin.dummytranslater.presentation;

import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.network.NetworkComponent;
import com.osminin.dummytranslater.network.TranslatorService;
import com.osminin.dummytranslater.network.modules.NetworkModule;
import com.osminin.dummytranslater.presentation.interfaces.TranslationPresenter;
import com.osminin.dummytranslater.ui.TranslationView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO: Add a class header comment!
 */

public final class TranslationPresenterImpl implements TranslationPresenter {

    private static final int INPUT_TIMEOUT = 300;
    private static final int INPUT_MIN = 2;
    private static final int INPUT_MAX = 10000;

    private static NetworkComponent networkComponent;

    @Inject
    TranslatorService mTranslatorService;
    private TranslationView mView;
    private Disposable mDisposable;

    @Override
    public void bind(TranslationView view) {
        mView = view;
        networkComponent = App.getComponent().plusNetworkComponent(new NetworkModule());
        networkComponent.inject(this);
    }

    @Override
    public void startObserveTextChanges(Observable<CharSequence> observable) {
        mDisposable = observable
                .filter(s -> s.length() > INPUT_MIN)
                .debounce(INPUT_TIMEOUT, TimeUnit.MILLISECONDS, Schedulers.io())
                .switchMap(s -> mTranslatorService.translate("en-ru", s.toString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> mView.onTextTranslated(s.getText()),
                        e -> e.printStackTrace());
    }

    @Override
    public void stopObserveTextChanges() {
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}