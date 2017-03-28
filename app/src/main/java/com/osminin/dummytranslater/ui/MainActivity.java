package com.osminin.dummytranslater.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.osminin.dummytranslater.R;
import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.models.Languages;
import com.osminin.dummytranslater.presentation.interfaces.MainPresenter;
import com.osminin.dummytranslater.ui.base.BaseActivity;
import com.osminin.dummytranslater.ui.custom.CustomAdapter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

import static android.R.attr.data;

public class MainActivity extends BaseActivity implements MainView {
    public static final String TRANSLATION_MODEL_KEY = "translation_model_extra";
    private static final int INPUT_TIMEOUT = 300;
    private static final int REQUEST_TRANSLATION_ACTIVITY = 100;

    @Inject
    MainPresenter mPresenter;

    @BindView(R.id.main_recent_list)
    RecyclerView mRecyclerView;

    private CustomAdapter mAdapter;
    private TranslationModel mTranslationModel;
    private View mInputContainer;
    private TextView mInputField;
    private View mTranslationContainer;
    private TextView mTranslationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        App.getAppComponent().inject(this);
        initList();
        mPresenter.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.startObserveUiEvents();
        //TODO: refactor
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
        mInputField.setText(text);
    }

    @Override
    public Observable<Object> textInputObservable() {
        return Observable
                .merge(RxView.clicks(mInputContainer), RxView.clicks(mInputField))
                .throttleFirst(INPUT_TIMEOUT,  TimeUnit.MILLISECONDS);
    }

    @Override
    public void showTranslationView() {
        Intent intent = new Intent(this, TranslationActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, mInputContainer, getString(R.string.main_translate_transition));
        if (mTranslationModel == null) {
            mTranslationModel = new TranslationModel();
            //TODO:
            mTranslationModel.setTranslationDirection(Languages.ENGLISH, Languages.RUSSIAN);
        }
        intent.putExtra(TRANSLATION_MODEL_KEY, mTranslationModel);
        startActivityForResult(intent, REQUEST_TRANSLATION_ACTIVITY, options.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TRANSLATION_ACTIVITY && resultCode == RESULT_OK) {
            TranslationModel model = data.getParcelableExtra(TRANSLATION_MODEL_KEY);
            mTranslationModel = model;
            mInputField.setText(model.getPrimaryText());
            //Translation Card
            if (mTranslationContainer != null) {
                mAdapter.removeTranslationCard(mTranslationContainer);
            }
            mTranslationContainer = View.inflate(this, R.layout.translation_card_layout, null);
            mTranslationField = ButterKnife.findById(mTranslationContainer, R.id.translate_result);
            //TODO:
            mTranslationField.setText(mTranslationModel.getTranslations().get(0));
            mAdapter.addTranslationCard(mTranslationContainer);
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
        //Input card
        mInputContainer = View.inflate(this, R.layout.input_card_layout, null);
        mInputField = ButterKnife.findById(mInputContainer, R.id.main_edit_text);
        mAdapter.addInputCard(mInputContainer);
    }
}
