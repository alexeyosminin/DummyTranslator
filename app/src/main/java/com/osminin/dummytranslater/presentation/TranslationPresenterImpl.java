package com.osminin.dummytranslater.presentation;

import android.view.KeyEvent;

import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.db.TranslationDataStore;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.network.TranslatorService;
import com.osminin.dummytranslater.presentation.interfaces.TranslationPresenter;
import com.osminin.dummytranslater.ui.TranslationView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * TODO: Add a class header comment!
 */

public final class TranslationPresenterImpl extends BasePresenterImpl<TranslationView>
        implements TranslationPresenter {

    private static final int INPUT_TIMEOUT = 300;
    private static final int INPUT_MIN = 1;
    private static final int INPUT_MAX = 10000;

    @Inject
    TranslatorService mTranslatorService;
    @Inject
    TranslationDataStore mDataStore;

    private TranslationModel mModel;

    @Override
    public void bind(TranslationView view) {
        super.bind(view);
        App.plusNetworkComponent().inject(this);
    }

    @Override
    public void startObserveUiChanges() {
        Timber.d("startObserveUiChanges: ");
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
                .filter(e -> e.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                .map(e -> mModel)
                .observeOn(Schedulers.single())
                .switchMap(mDataStore::open)
                .switchMap(mDataStore::add)
                .switchMap(mDataStore::close)
                .observeOn(AndroidSchedulers.mainThread())
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
        Timber.d("stopObserveUiChanges: ");
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void setTranslationModel(TranslationModel model) {
        Timber.d("setTranslationModel: ");
        mModel = model;
        mView.setTranslationHintFrom(mModel.getTranslationDirection().first);
        mView.setTranslationHintTo(mModel.getTranslationDirection().second);
    }

    @Override
    public void destroy() {
        Timber.d("destroy: ");
        App.clearNetworkComponent();
    }

    private void updateModel(String primaryText, List<String> translations) {
        Timber.d("updateModel: ");
        if (mModel != null) {
            mModel.setPrimaryText(primaryText);
            mModel.setTranslations(translations);
        }
    }

    private void handleError(Throwable t) {
        Timber.d("handleError: ");
        //restart subscription
        if (t instanceof java.io.InterruptedIOException ||
                t instanceof java.net.SocketTimeoutException) {
            stopObserveUiChanges();
            startObserveUiChanges();
        }
        t.printStackTrace();
    }
}
