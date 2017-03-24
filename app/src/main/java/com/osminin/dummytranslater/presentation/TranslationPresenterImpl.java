package com.osminin.dummytranslater.presentation;

import android.view.KeyEvent;

import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.db.interfaces.TranslationDataStore;
import com.osminin.dummytranslater.network.TranslatorService;
import com.osminin.dummytranslater.presentation.interfaces.TranslationPresenter;
import com.osminin.dummytranslater.ui.TranslationView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

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
    @Inject
    TranslationDataStore mDataStore;
    private TranslationView mView;
    private CompositeDisposable mDisposable;

    private String mTranslationDirection;

    @Override
    public void bind(TranslationView view) {
        mView = view;
        App.plusNetworkComponent().inject(this);
    }

    @Override
    public void startObserveUiChanges() {
        verifyDisposable();
        mDisposable.add(mView.inputTextChanges()
                .filter(charSequence -> charSequence.length() > INPUT_MIN)
                .debounce(INPUT_TIMEOUT, TimeUnit.MILLISECONDS, Schedulers.io())
                .map(CharSequence::toString)
                .switchMap(string -> mTranslatorService.translate(mTranslationDirection, string))
                .map(responseModel -> responseModel.fromNetworkModel())
                .switchMap(mView::onTextTranslated)
                .observeOn(Schedulers.io())
                .sample(mView.softKeyEvents()
                        .filter(e -> e.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                .switchMap(mDataStore::add)
                .switchMap(mView::onTextInputStop)
                .doOnError(this::handleError)
                .subscribe());
    }

    @Override
    public void stopObserveUiChanges() {
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void setTranslationDirection(String direction) {
        mTranslationDirection = direction;
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

    private void handleError(Throwable t) {
        //restart subscription
        /*stopObserveUiChanges();
        startObserveUiChanges();
        e.printStackTrace();*/
    }
}
