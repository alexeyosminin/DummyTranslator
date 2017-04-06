package com.osminin.dummytranslater.presentation;

import android.view.KeyEvent;

import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.db.interfaces.TranslationDataStore;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.network.TranslatorService;
import com.osminin.dummytranslater.presentation.interfaces.TranslationPresenter;
import com.osminin.dummytranslater.ui.TranslationView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO: Add a class header comment!
 */

public final class TranslationPresenterImpl implements TranslationPresenter {

    private static final int INPUT_TIMEOUT = 300;
    private static final int INPUT_MIN = 1;
    private static final int INPUT_MAX = 10000;

    @Inject
    TranslatorService mTranslatorService;
    @Inject
    TranslationDataStore mDataStore;

    private TranslationView mView;
    private CompositeDisposable mDisposable;
    private TranslationModel mModel;

    @Override
    public void bind(TranslationView view) {
        mView = view;
        App.plusNetworkComponent().inject(this);
    }

    @Override
    public void startObserveUiChanges() {
        verifyDisposable();
        //start observe text input
        mDisposable.add(mView.inputTextChanges()
                .switchMap(mView::showCrossButton)
                .filter(charSequence -> charSequence.length() > INPUT_MIN)
                .debounce(INPUT_TIMEOUT, TimeUnit.MILLISECONDS, Schedulers.io())
                .map(CharSequence::toString)
                .flatMap(mView::showProgress)
                .doOnNext(string -> updateModel(string, null))
                .observeOn(Schedulers.io())
                .switchMap(string -> mTranslatorService.translate(mModel))
                .switchMap(mView::onTextTranslated)
                .doOnError(this::handleError)
                .subscribe(model -> mModel = model));
        //start observe 'Enter' key button on virtual keyboard
        mDisposable.add(mView.softEnterKeyEvents()
                .map(e -> mModel)
                .switchMap(mDataStore::add)
                .switchMap(mView::onTextInputStop)
                .doOnError(this::handleError)
                .subscribe());
        //start observe cross btn on text input field
        mDisposable.add(mView.crossButtonClicks()
                .throttleFirst(INPUT_TIMEOUT, TimeUnit.MILLISECONDS)
                .doOnNext(i -> updateModel(null, null))
                .switchMap(mView::clearInputOutputFields)
                .subscribe());

        mDisposable.add(mView.backButtonClicks()
                .throttleFirst(INPUT_TIMEOUT, TimeUnit.MILLISECONDS)
                .map(e -> mModel)
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
    public void setTranslationModel(TranslationModel model) {
        mModel = model;
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

    private void updateModel(String primaryText, List<String> translations) {
        if (mModel != null) {
            mModel.setPrimaryText(primaryText);
            mModel.setTranslations(translations);
        }
    }

    private void handleError(Throwable t) {
        //restart subscription
        if (t instanceof java.io.InterruptedIOException ||
                t instanceof java.net.SocketTimeoutException) {
            stopObserveUiChanges();
            startObserveUiChanges();
        }
        t.printStackTrace();
    }
}
