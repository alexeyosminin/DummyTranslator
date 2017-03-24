package com.osminin.dummytranslater.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.osminin.dummytranslater.R;
import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.presentation.interfaces.MainPresenter;
import com.osminin.dummytranslater.ui.base.BaseActivity;
import com.osminin.dummytranslater.ui.custom.CustomAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainView {

    @Inject
    MainPresenter mPresenter;

    @BindView(R.id.main_edit_text)
    TextView mTranslationField;
    @BindView(R.id.main_recent_list)
    RecyclerView mRecyclerView;

    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        App.getAppComponent().inject(this);
        mPresenter.bind(this);
        initList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.startObserveTextInput(RxView.clicks(mTranslationField));
        mPresenter.startObserveRecentClicks(mAdapter.getViewClickedObservable());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.stopObserveUiEvents();
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
        startActivityForResult(intent, 100, options.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            TranslationModel model = data.getParcelableExtra("res");
            mTranslationField.setText(model.getTranslations().get(0));
        }
    }

    private void initList() {
        mAdapter = new CustomAdapter();
        RecyclerView.LayoutManager layoutManager;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
