package com.osminin.dummytranslater.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import oxim.digital.rx2anim.RxAnimations;

import static oxim.digital.rx2anim.RxAnimations.animateTogether;
import static oxim.digital.rx2anim.RxAnimations.fadeIn;
import static oxim.digital.rx2anim.RxAnimations.fadeOut;

public class MainActivity extends BaseActivity implements MainView {
    public static final String TRANSLATION_MODEL_KEY = "translation_model_extra";
    private static final int INPUT_TIMEOUT = 300;
    private static final int REQUEST_TRANSLATION_ACTIVITY = 100;
    private static final int ANIMATION_DURATION = 200;

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
    private View mClearInputBtn;
    private ImageView mFavoriteStar;

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
        mAdapter.clearRecent();
        mPresenter.stopObserveUiEvents();
    }

    @Override
    public void showError() {
        //TODO:
        Log.d("", "");
    }

    @Override
    public Observable<Object> textInputObservable() {
        return Observable
                .merge(RxView.clicks(mInputContainer), RxView.clicks(mInputField))
                .throttleFirst(INPUT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public Observable<Object> translationDirectionObservable() {
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
    public Observable<TranslationModel> recentItemsObservable() {
        return mAdapter.getViewClickedObservable();
    }

    @Override
    public Observable<TranslationModel> activityResultObservable() {
        return mActivityResultSubject;
    }

    @Override
    public Observable<Object> clearInputObservable() {
        return RxView.clicks(mClearInputBtn)
                .throttleFirst(INPUT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public Observable<Object> favoriteStarObservable() {
        return RxView.clicks(mFavoriteStar)
                .throttleFirst(INPUT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public <T> Observable<T> changeTransDirection(T item) {
        int pos = mFromSpinner.getSelectedItemPosition();
        return Observable.just(item)
                .doOnNext(i -> RxAnimations.animateTogether(fadeOut(mFromSpinner, ANIMATION_DURATION),
                        fadeOut(mToSpinner, ANIMATION_DURATION)
                                .doOnComplete(() -> mFromSpinner.setSelection(mToSpinner.getSelectedItemPosition()))
                                .doOnComplete(() -> mToSpinner.setSelection(pos))
                                .concatWith(animateTogether(fadeIn(mFromSpinner, ANIMATION_DURATION),
                                        fadeIn(mToSpinner, ANIMATION_DURATION)))
                ).subscribe());
    }

    @Override
    public <T> Observable<T> clearRecentList(T item) {
        return Observable.just(item)
                .doOnNext(i -> mAdapter.clearRecent());
    }

    @Override
    public <T> Observable<T> clearInputCard(T item) {
        return Observable.just(item)
                .doOnNext(t -> {
                    mInputField.setText("");
                    mAdapter.removeTranslationCard(mTranslationContainer);
                });
    }

    @Override
    public Observable<Boolean> setFavorite(Boolean isFavorite) {
        return Observable.just(isFavorite)
                .doOnNext(f -> mFavoriteStar.setImageResource(
                        isFavorite ? R.drawable.ic_star_orange_24dp
                                : R.drawable.ic_star_border_white_24dp
                ));
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
    public Observable<TranslationModel> setTranslation(TranslationModel model) {
        return Observable.just(model)
                .doOnNext(this::addTranslationCard)
                .doOnNext(m -> mTranslationField.setText(m.getTranslations().get(0)))
                .doOnNext(m -> mFavoriteStar.setImageResource(
                        m.isFavorite() ? R.drawable.ic_star_orange_24dp
                                : R.drawable.ic_star_border_white_24dp
                ));
    }

    @Override
    public Observable<TranslationModel> addRecentItem(TranslationModel model) {
        return Observable.just(model)
                .doOnNext(mAdapter::addRecentItem);
    }

    @Override
    public Observable<TranslationModel> updateRecentItem(TranslationModel model) {
        return null;
    }

    @Override
    public Observable<TranslationModel> setDefaultTranslationDirection(TranslationModel model) {
        return Observable.just(model)
                .doOnNext(this::setDefaultTranslation);
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        //Input card
        initInputCard();
        //Translation card
        initTranslationCard();
    }

    private void initInputCard() {
        mInputContainer = View.inflate(this, R.layout.input_card_layout, null);
        mInputField = ButterKnife.findById(mInputContainer, R.id.input_edit_text);
        mAdapter.addInputCard(mInputContainer);
        mFromSpinner = ButterKnife.findById(mInputContainer, R.id.input_from_spinner);
        mToSpinner = ButterKnife.findById(mInputContainer, R.id.input_to_spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Languages.getHRStrings(this));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFromSpinner.setAdapter(spinnerArrayAdapter);
        mToSpinner.setAdapter(spinnerArrayAdapter);
        mReversTransDirectionBtn = ButterKnife.findById(mInputContainer, R.id.input_reverse_direction);
        mClearInputBtn = ButterKnife.findById(mInputContainer, R.id.input_clear_btn);
    }

    private void initTranslationCard() {
        mTranslationContainer = View.inflate(this, R.layout.translation_card_layout, null);
        mTranslationField = ButterKnife.findById(mTranslationContainer, R.id.translate_result);
        mFavoriteStar = ButterKnife.findById(mTranslationContainer, R.id.translate_favorite_star);
    }

    private <T> void addTranslationCard(T item) {
        if (mTranslationContainer != null) {
            mAdapter.removeTranslationCard(mTranslationContainer);
            mAdapter.addTranslationCard(mTranslationContainer);
        }
    }

    private void setDefaultTranslation(TranslationModel model) {
        mFromSpinner.setSelection(model.getTranslationDirection().first.ordinal());
        mToSpinner.setSelection(model.getTranslationDirection().second.ordinal());
    }
}
