package com.osminin.dummytranslater.presentation;

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

    private static NetworkComponent networkComponent;

    @Inject
    TranslatorService mTranslatorService;

    private MainView mView;
    private Disposable mDisposable;

    @Override
    public void bind(MainView view) {
        mView = view;
        networkComponent = App.getComponent().plusNetworkComponent(new NetworkModule());
        networkComponent.inject(this);
    }

    @Override
    public void startObserveTextChanges(Observable<CharSequence> observable) {
        mDisposable = observable
                .filter(s -> s.length() > 2)
                .debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .switchMap(s -> mTranslatorService.translate("en-ru", s.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> mView.onTextTranslated(s.getText().get(0)),
                        e -> e.printStackTrace());
    }

    @Override
    public void stopObserveTextChanges() {
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
