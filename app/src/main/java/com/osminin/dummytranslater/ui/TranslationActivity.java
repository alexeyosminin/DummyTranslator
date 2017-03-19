package com.osminin.dummytranslater.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.osminin.dummytranslater.R;
import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.presentation.interfaces.TranslationPresenter;
import com.osminin.dummytranslater.ui.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO: Add a class header comment!
 */

public final class TranslationActivity extends BaseActivity implements TranslationView {

    @Inject
    TranslationPresenter mPresenter;

    @BindView(R.id.translate_edit_text)
    EditText mTranlationInput;
    @BindView(R.id.translate_result)
    TextView mTranslationResult;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        ButterKnife.bind(this);
        App.getComponent().inject(this);
        mPresenter.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.startObserveTextChanges(RxTextView.textChanges(mTranlationInput));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.stopObserveTextChanges();
    }

    @Override
    public void onTextTranslated(List<String> text) {
        String resText = "";
        for (String str : text) {
            resText = str + "\n";
        }
        mTranslationResult.setText(resText);
    }
}
