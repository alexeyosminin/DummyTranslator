package com.osminin.dummytranslater.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.osminin.dummytranslater.R;
import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.presentation.interfaces.MainPresenter;
import com.osminin.dummytranslater.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainView {

    @Inject
    MainPresenter mPresenter;

    @BindView(R.id.main_edit_text)
    TextView mTranslationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        App.getComponent().inject(this);
        mPresenter.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.startObserveTextInput(RxView.clicks(mTranslationField));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void showError() {

    }

    @Override
    public void onTextTranslated(String text) {
        mTranslationField.setText(text);
    }

    @Override
    public void showTranslationView() {
        Intent intent = new Intent(this, TranslationActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, mTranslationField, getString(R.string.main_translate_transition));
        startActivity(intent, options.toBundle());
    }
}
