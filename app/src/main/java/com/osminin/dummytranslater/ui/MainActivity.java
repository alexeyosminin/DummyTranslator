package com.osminin.dummytranslater.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.osminin.dummytranslater.R;
import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.models.Languages;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.presentation.interfaces.MainPresenter;
import com.osminin.dummytranslater.ui.base.BaseActivity;
import com.osminin.dummytranslater.ui.custom.CustomAdapter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends BaseActivity implements MainView {
    public static final String TRANSLATION_MODEL_KEY = "translation_model_extra";
    private static final int INPUT_TIMEOUT = 300;
    private static final int REQUEST_TRANSLATION_ACTIVITY = 100;

    @Inject
    MainPresenter mPresenter;

    @BindView(R.id.main_recent_list)
    RecyclerView mRecyclerView;

    private CustomAdapter mAdapter;

    //card controls
    private View mInputContainer;
    private TextView mInputField;
    private View mTranslationContainer;
    private TextView mTranslationField;
    private Spinner mFromSpinner;
    private Spinner mToSpinner;
    private View mReversTransDirectionBtn;

    private PublishSubject<TranslationModel> mActivityResultSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        App.getAppComponent().inject(this);
        initList();
        mActivityResultSubject = PublishSubject.create();
        mPresenter.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityResultSubject.onComplete();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.startObserveUiEvents();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.stopObserveUiEvents();
    }

    @Override
    public void showError() {
        Log.d("", "");
    }

    @Override
    public void onTextTranslated(String text) {
        mInputField.setText(text);
    }

    @Override
    public Observable<Object> textInputObservable() {
        return Observable
                .merge(RxView.clicks(mInputContainer), RxView.clicks(mInputField))
                .throttleFirst(INPUT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public Observable<Object> changeTranslationDirectionClicks() {
        return RxView.clicks(mReversTransDirectionBtn)
                .throttleFirst(INPUT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public Observable<Languages> fromSpinnerObservable() {
        return RxAdapterView.itemSelections(mFromSpinner)
                .map(i -> Languages.values()[i]);
    }

    @Override
    public Observable<Languages> toSpinnerObservable() {
        return RxAdapterView.itemSelections(mToSpinner)
                .map(i -> Languages.values()[i]);
    }

    @Override
    public Observable<TranslationModel> getRecentItemsClicks() {
        return mAdapter.getViewClickedObservable();
    }

    @Override
    public Observable<TranslationModel> onActivityResult() {
        return mActivityResultSubject;
    }

    @Override
    public <T> Observable<T> changeTransDirection(T item) {
        //TODO:
        return Observable.just(item);
    }

    @Override
    public Observable<TranslationModel> showTranslationView(TranslationModel model) {
        return Observable.just(model)
                .doOnNext(this::launchTranslationView);
    }

    @Override
    public Observable<TranslationModel> setPrimaryText(TranslationModel model) {
        return Observable.just(model)
                .doOnNext(m -> mInputField.setText(m.getPrimaryText()));
    }

    @Override
    public Observable<TranslationModel> setTranslationText(TranslationModel model) {
        return Observable.just(model)
                .doOnNext(this::addTranslationCard)
                .doOnNext(m -> mTranslationField.setText(m.getTranslations().get(0)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TRANSLATION_ACTIVITY && resultCode == RESULT_OK) {
            TranslationModel model = data.getParcelableExtra(TRANSLATION_MODEL_KEY);
            mActivityResultSubject.onNext(model);
        }
    }

    private void launchTranslationView(TranslationModel model) {
        Intent intent = new Intent(this, TranslationActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, mInputContainer, getString(R.string.main_translate_transition));
        intent.putExtra(TRANSLATION_MODEL_KEY, model);
        startActivityForResult(intent, REQUEST_TRANSLATION_ACTIVITY, options.toBundle());
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
        mInputField = ButterKnife.findById(mInputContainer, R.id.input_edit_text);
        mAdapter.addInputCard(mInputContainer);
        //Translation Card init
        mTranslationContainer = View.inflate(this, R.layout.translation_card_layout, null);
        mTranslationField = ButterKnife.findById(mTranslationContainer, R.id.translate_result);
        initSpinners();
    }

    private void initSpinners() {
        mFromSpinner = ButterKnife.findById(mInputContainer, R.id.input_from_spinner);
        mToSpinner = ButterKnife.findById(mInputContainer, R.id.input_to_spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Languages.getHRStrings(this));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFromSpinner.setAdapter(spinnerArrayAdapter);
        mToSpinner.setAdapter(spinnerArrayAdapter);
        initReversTransDirectionBtn();
    }

    private void initReversTransDirectionBtn() {
        mReversTransDirectionBtn = ButterKnife.findById(mInputContainer, R.id.input_reverse_direction);
    }

    private <T> void addTranslationCard(T item) {
        if (mTranslationContainer != null) {
            mAdapter.removeTranslationCard(mTranslationContainer);
            mAdapter.addTranslationCard(mTranslationContainer);
        }
    }
}
