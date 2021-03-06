package com.osminin.dummytranslater.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.osminin.dummytranslater.R;
import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.models.Languages;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.presentation.interfaces.TranslationPresenter;
import com.osminin.dummytranslater.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import static com.osminin.dummytranslater.Config.TRANSLATION_MODEL_KEY;

/**
 * TODO: Add a class header comment!
 */

public final class TranslationActivity extends BaseActivity implements TranslationView {

    @Inject
    TranslationPresenter mPresenter;

    @BindView(R.id.translate_text_view)
    EditText mTranslationInput;
    @BindView(R.id.translate_result)
    TextView mTranslationResult;
    @BindView(R.id.translate_cross_button)
    View mCrossButton;
    @BindView(R.id.translate_progress)
    View mProgress;

    private PublishSubject<Object> mBackButtonSubject;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Timber.d("onCreate: ");
        setContentView(R.layout.activity_translation);
        ButterKnife.bind(this);
        App.getAppComponent().inject(this);
        mPresenter.bind(this);
        TranslationModel translationModel = getIntent().getParcelableExtra(TRANSLATION_MODEL_KEY);
        mPresenter.setTranslationModel(translationModel);
        String translationText = translationModel.getPrimaryText();
        mTranslationInput.setText(translationText);
        mBackButtonSubject = PublishSubject.create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.d("onResume: ");
        mPresenter.startObserveUiChanges();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Timber.d("onPause: ");
        mPresenter.stopObserveUiChanges();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy: ");
        mPresenter.destroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Timber.d("onBackPressed: ");
        mBackButtonSubject.onNext("");
    }

    @Override
    public Observable<TranslationModel> onTextInputStop(TranslationModel model) {
        Timber.d("onTextInputStop: ");
        return Observable.just(model)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(this::finishAnimated);

    }

    @Override
    public <T> Observable<T> showProgress(T item) {
        Timber.d("showProgress: ");
        return Observable.just(item)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(this::showProgressInternal);
    }

    @Override
    public <T> Observable<T> showCrossButton(T item) {
        Timber.d("showCrossButton: ");
        return Observable.just(item)
                .doOnNext(this::showCrossBtn);
    }

    @Override
    public Observable<CharSequence> inputTextChanges() {
        Timber.d("inputTextChanges: ");
        return RxTextView.textChanges(mTranslationInput);
    }

    @Override
    public Observable<KeyEvent> softEnterKeyEvents() {
        Timber.d("softEnterKeyEvents: ");
        return RxView.keys(mTranslationInput, e -> e.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<TranslationModel> onTextTranslated(TranslationModel model) {
        Timber.d("onTextTranslated: ");
        return Observable.just(model)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(this::setTranslatedText);
    }

    @Override
    public Observable<Object> crossButtonClicks() {
        Timber.d("crossButtonClicks: ");
        return RxView.clicks(mCrossButton);
    }

    @Override
    public Observable<Object> backButtonClicks() {
        Timber.d("backButtonClicks: ");
        return mBackButtonSubject;
    }

    @Override
    public void setTranslationHintFrom(Languages language) {
        mTranslationInput.setHint(language.getHRres());
    }

    @Override
    public void setTranslationHintTo(Languages language) {
        mTranslationResult.setHint(language.getHRres());
    }

    @Override
    public <T> Observable<T> clearInputOutputFields(T item) {
        Timber.d("clearInputOutputFields: ");
        return Observable.just(item)
                .doOnNext(this::clearInputOutput);
    }

    private void setTranslatedText(TranslationModel model) {
        Timber.d("setTranslatedText: ");
        if (model.getTranslations() != null && !model.getTranslations().isEmpty()) {
            mTranslationResult.setText(model.getTranslations().get(0));
        }
    }

    private void finishAnimated(TranslationModel model) {
        Timber.d("finishAnimated: ");
        setResult(RESULT_OK, new Intent().putExtra(TRANSLATION_MODEL_KEY, model));
        supportFinishAfterTransition();
    }

    private <T> void showProgressInternal(T item) {
        Timber.d("showProgressInternal: ");
        mProgress.setVisibility(View.VISIBLE);
        showCrossBtn(item);
    }

    public <T> Observable<T> hideProgress(T item) {
        Timber.d("hideProgress: ");
        return Observable.just(item)
                .doOnNext(t -> {
                    mProgress.setVisibility(View.GONE);
                });

    }

    @Override
    public <T> Observable<T> hideKeyboard(T item) {
        return Observable.just(item)
                .doOnNext(t -> {
                    View view = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                });
    }

    private <T> void clearInputOutput(T item) {
        Timber.d("clearInputOutput: ");
        mTranslationInput.setText("");
        mTranslationResult.setText("");
    }

    private <T> void showCrossBtn(T item) {
        Timber.d("showCrossBtn: ");
        if (mTranslationInput.getText().length() != 0 && mProgress.getVisibility() == View.GONE) {
            mCrossButton.setVisibility(View.VISIBLE);
        } else {
            mCrossButton.setVisibility(View.GONE);
        }
    }
}
