package com.osminin.dummytranslater.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
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
    EditText mEditText;
    @BindView(R.id.main_text_view)
    TextView mTextView;

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
        mPresenter.startObserveTextChanges(RxTextView.textChanges(mEditText));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.stopObserveTextChanges();
    }

    @Override
    public void showError() {

    }

    @Override
    public void onTextTranslated(String text) {
        mTextView.setText(text);
    }
}
