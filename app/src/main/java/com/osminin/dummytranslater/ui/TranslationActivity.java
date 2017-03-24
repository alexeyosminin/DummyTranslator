package com.osminin.dummytranslater.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        ButterKnife.bind(this);
        App.getAppComponent().inject(this);
        mPresenter.bind(this);
        //TODO:
        mPresenter.setTranslationDirection("en-ru");
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
    public Observable<CharSequence> inputTextChanges() {
        return RxTextView.textChanges(mTranslationInput);
    }

    @Override
    public Observable<KeyEvent> softKeyEvents() {
        return  RxView.keys(mTranslationInput);
    }

    @Override
    public Observable<TranslationModel> onTextTranslated(TranslationModel model) {
        return Observable.just(model)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(this::setTranslatedText);
    }

    private void setTranslatedText(TranslationModel model) {
        if (model.getTranslations() != null && !model.getTranslations().isEmpty()) {
            mTranslationResult.setText(model.getTranslations().get(0));
        }
    }

    private void finishAnimated(TranslationModel model) {
        setResult(RESULT_OK, new Intent().putExtra("res", model));
        supportFinishAfterTransition();
    }
}
