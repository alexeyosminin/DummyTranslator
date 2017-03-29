package com.osminin.dummytranslater.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.osminin.dummytranslater.R;
import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.presentation.interfaces.TranslationPresenter;
import com.osminin.dummytranslater.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.osminin.dummytranslater.ui.MainActivity.TRANSLATION_MODEL_KEY;

/**
 * TODO: Add a class header comment!
 */

public final class TranslationActivity extends BaseActivity implements TranslationView {

    @Inject
    TranslationPresenter mPresenter;

    @BindView(R.id.translate_edit_text)
    EditText mTranslationInput;
    @BindView(R.id.translate_result)
    TextView mTranslationResult;
    @BindView(R.id.translate_cross_button)
    View mCrossButton;
    @BindView(R.id.translate_progress)
    View mProgress;

    @BindString(R.string.main_tap_to_enter)
    String mDefaultInput;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        ButterKnife.bind(this);
        App.getAppComponent().inject(this);
        mPresenter.bind(this);
        TranslationModel translationModel = getIntent().getParcelableExtra(TRANSLATION_MODEL_KEY);
        mPresenter.setTranslationModel(translationModel);
        String translationText = translationModel.getPrimaryText();
        mTranslationInput.setText(translationText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.startObserveUiChanges();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.stopObserveUiChanges();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public Observable<TranslationModel> onTextInputStop(TranslationModel model) {
        return Observable.just(model)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(this::finishAnimated);

    }

    @Override
    public <T> Observable<T> showProgress(T item) {
        return Observable.just(item)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(this::showProgressInternal);
    }

    @Override
    public <T> Observable<T> showCrossButton(T item) {
        return Observable.just(item)
                .doOnNext(this::showCrossBtn);
    }

    @Override
    public Observable<CharSequence> inputTextChanges() {
        return RxTextView.textChanges(mTranslationInput);
    }

    @Override
    public Observable<KeyEvent> softKeyEvents() {
        return RxView.keys(mTranslationInput)
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<TranslationModel> onTextTranslated(TranslationModel model) {
        return Observable.just(model)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(this::setTranslatedText)
                .doOnNext(this::hideProgress);
    }

    @Override
    public Observable<Object> crossButtonClicks() {
        return RxView.clicks(mCrossButton);
    }

    @Override
    public <T> Observable<T> clearInputOutputFields(T item) {
        return Observable.just(item)
                .doOnNext(this::clearInputOutput);
    }

    private void setTranslatedText(TranslationModel model) {
        if (model.getTranslations() != null && !model.getTranslations().isEmpty()) {
            mTranslationResult.setText(model.getTranslations().get(0));
        }
    }

    private void finishAnimated(TranslationModel model) {
        setResult(RESULT_OK, new Intent().putExtra(TRANSLATION_MODEL_KEY, model));
        supportFinishAfterTransition();
    }

    private <T> void showProgressInternal(T item) {
        mProgress.setVisibility(View.VISIBLE);
        showCrossBtn(item);
    }

    private <T> void hideProgress(T item) {
        mProgress.setVisibility(View.GONE);
        showCrossBtn(item);
    }

    private <T> void clearInputOutput(T item) {
        mTranslationInput.setText("");
        mTranslationResult.setText("");
    }

    private <T> void showCrossBtn(T item) {
        if (mTranslationInput.getText().length() != 0 && mProgress.getVisibility() == View.GONE) {
            mCrossButton.setVisibility(View.VISIBLE);
        } else {
            mCrossButton.setVisibility(View.GONE);
        }
    }
}
