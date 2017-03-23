package com.osminin.dummytranslater.presentation;

import android.view.KeyEvent;

import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.network.TranslatorService;
import com.osminin.dummytranslater.presentation.interfaces.TranslationPresenter;
import com.osminin.dummytranslater.ui.TranslationView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO: Add a class header comment!
 */

public final class TranslationPresenterImpl implements TranslationPresenter {

    private static final int INPUT_TIMEOUT = 300;
    private static final int INPUT_MIN = 2;
    private static final int INPUT_MAX = 10000;

    @Inject
    TranslatorService mTranslatorService;
    private TranslationView mView;
    private CompositeDisposable mDisposable;

    @Override
    public void bind(TranslationView view) {
        mView = view;
        App.plusNetworkComponent().inject(this);
    }

    @Override
    public void startObserveTextChanges(final Observable<CharSequence> observable) {
        verifyDisposable();
        mDisposable.add(observable
                .filter(s -> s.length() > INPUT_MIN)
                .debounce(INPUT_TIMEOUT, TimeUnit.MILLISECONDS, Schedulers.io())
                .switchMap(s -> mTranslatorService.translate("en-ru", s.toString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> mView.onTextTranslated(s.getText()),
                        e -> {
                            //restart subscription
                            stopObserveTextChanges();
                            startObserveTextChanges(observable);
                            e.printStackTrace();
                        }
                ));
    }

    @Override
    public void stopObserveTextChanges() {
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void startObserveTextChangesCompleted(Observable<KeyEvent> observable) {
        verifyDisposable();
        mDisposable.add(observable
                .filter(e -> e.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                .subscribe((res) -> mView.onTextInputStop(res)));
    }

    @Override
    public void destroy() {
        App.clearNetworkComponent();
    }

    private void verifyDisposable() {
        if (mDisposable == null || mDisposable.isDisposed()) {
            mDisposable = new CompositeDisposable();
        }
    }
}
