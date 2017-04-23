package com.osminin.dummytranslater.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import timber.log.Timber;

import static com.osminin.dummytranslater.Config.TRANSLATION_MODEL_KEY;
import static oxim.digital.rx2anim.RxAnimations.animateTogether;
import static oxim.digital.rx2anim.RxAnimations.fadeIn;
import static oxim.digital.rx2anim.RxAnimations.fadeOut;

public class MainActivity extends BaseActivity implements MainView {
    private static final int INPUT_TIMEOUT = 300;
    private static final int REQUEST_TRANSLATION_ACTIVITY = 100;
    private static final int REQUEST_FAVORITES_ACTIVITY = 200;
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
    private PublishSubject<Integer> mOptionsSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        Timber.d("onCreate: ");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        App.getAppComponent().inject(this);
        initList();
        mActivityResultSubject = PublishSubject.create();
        mOptionsSubject = PublishSubject.create();
        mPresenter.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy: ");
        mActivityResultSubject.onComplete();
        mPresenter.destroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.d("onStart: ");
        mPresenter.startObserveUiEvents();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.d("onStop: ");
        mAdapter.clearRecent();
        mPresenter.stopObserveUiEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Timber.d("onOptionsItemSelected: ");
        mOptionsSubject.onNext(item.getItemId());
        return true;
    }

    @Override
    public void showError() {
        //TODO:
        Timber.d("showError: ");
    }

    @Override
    public Observable<Object> textInputObservable() {
        Timber.d("textInputObservable: ");
        return Observable
                .merge(RxView.clicks(mInputContainer), RxView.clicks(mInputField))
                .throttleFirst(INPUT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public Observable<Object> translationDirectionObservable() {
        Timber.d("translationDirectionObservable: ");
        return RxView.clicks(mReversTransDirectionBtn)
                .throttleFirst(INPUT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public Observable<Languages> fromSpinnerObservable() {
        Timber.d("fromSpinnerObservable: ");
        return RxAdapterView.itemSelections(mFromSpinner)
                .map(i -> Languages.values()[i]);
    }

    @Override
    public Observable<Languages> toSpinnerObservable() {
        Timber.d("toSpinnerObservable: ");
        return RxAdapterView.itemSelections(mToSpinner)
                .map(i -> Languages.values()[i]);
    }

    @Override
    public Observable<TranslationModel> recentItemsObservable() {
        Timber.d("recentItemsObservable: ");
        return mAdapter.getViewClickedObservable();
    }

    @Override
    public Observable<TranslationModel> activityResultObservable() {
        Timber.d("activityResultObservable: ");
        return mActivityResultSubject;
    }

    @Override
    public Observable<Object> clearInputObservable() {
        Timber.d("clearInputObservable: ");
        return RxView.clicks(mClearInputBtn)
                .throttleFirst(INPUT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public Observable<Object> favoriteStarObservable() {
        Timber.d("favoriteStarObservable: ");
        return RxView.clicks(mFavoriteStar)
                .throttleFirst(INPUT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public Observable<Integer> optionsMenuObservable() {
        return mOptionsSubject;
    }

    @Override
    public <T> Observable<T> changeTransDirection(T item) {
        Timber.d("changeTransDirection: ");
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
        Timber.d("clearRecentList: ");
        return Observable.just(item)
                .doOnNext(i -> mAdapter.clearRecent());
    }

    @Override
    public <T> Observable<T> clearInputCard(T item) {
        Timber.d("clearInputCard: ");
        return Observable.just(item)
                .doOnNext(t -> {
                    mInputField.setText("");
                    mAdapter.removeTranslationCard(mTranslationContainer);
                });
    }

    @Override
    public <T> Observable<T> showFavoritesView(T item) {
        Timber.d("showFavoritesView: ");
        return Observable.just(item)
                .doOnNext(this::launchFavoritesView);
    }

    @Override
    public <T> Observable<T> requestInputFocus(T item) {
        return Observable.just(item)
                .doOnNext(t -> mInputField.requestFocus());
    }

    @Override
    public Observable<Boolean> setFavorite(Boolean isFavorite) {
        Timber.d("setFavorite: ");
        return Observable.just(isFavorite)
                .doOnNext(f -> mFavoriteStar.setImageResource(
                        isFavorite ? R.drawable.ic_star_orange_24dp
                                : R.drawable.ic_star_border_white_24dp
                ));
    }

    @Override
    public Observable<TranslationModel> showTranslationView(TranslationModel model) {
        Timber.d("showTranslationView: ");
        return Observable.just(model)
                .doOnNext(this::launchTranslationView);
    }

    @Override
    public Observable<TranslationModel> setPrimaryText(TranslationModel model) {
        Timber.d("setPrimaryText: ");
        return Observable.just(model)
                .doOnNext(m -> mInputField.setText(m.getPrimaryText()));
    }

    @Override
    public Observable<TranslationModel> setTranslation(TranslationModel model) {
        Timber.d("setTranslation: %s", model);
        return Observable.just(model)
                //.filter(model1 -> !model.getTranslations().isEmpty())
                .doOnNext(this::addTranslationCard)
                .doOnNext(m -> mTranslationField.setText(m.getTranslations().get(0)))
                .doOnNext(m -> mFavoriteStar.setImageResource(
                        m.isFavorite() ? R.drawable.ic_star_orange_24dp
                                : R.drawable.ic_star_border_white_24dp
                ));
    }

    @Override
    public Observable<TranslationModel> addRecentItem(TranslationModel model) {
        Timber.d("addRecentItem: %s", model);
        return Observable.just(model)
                .doOnNext(mAdapter::addRecentItem);
    }

    @Override
    public Observable<TranslationModel> updateRecentItem(TranslationModel item) {
        Timber.d("updateRecentItem: %s", item);
        return Observable.just(item)
                .doOnNext(mAdapter::updateRecentItem);
    }

    @Override
    public Observable<TranslationModel> setDefaultTranslationDirection(TranslationModel model) {
        Timber.d("setDefaultTranslationDirection: ");
        return Observable.just(model)
                .doOnNext(this::setDefaultTranslation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.d("onActivityResult: ");
        if ((requestCode == REQUEST_TRANSLATION_ACTIVITY
                    || requestCode == REQUEST_FAVORITES_ACTIVITY)
                && resultCode == RESULT_OK) {
            TranslationModel model = data.getParcelableExtra(TRANSLATION_MODEL_KEY);
            mActivityResultSubject.onNext(model);
        }
    }

    private void launchTranslationView(TranslationModel model) {
        Timber.d("launchTranslationView: ");
        Intent intent = new Intent(this, TranslationActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, mInputContainer, mInputContainer.getTransitionName());
        intent.putExtra(TRANSLATION_MODEL_KEY, model);
        startActivityForResult(intent, REQUEST_TRANSLATION_ACTIVITY, options.toBundle());
    }

    private <T> void launchFavoritesView(T item) {
        Timber.d("launchFavoritesView: ");
        Intent intent = new Intent(this, FavoritesActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeBasic();
        startActivityForResult(intent, REQUEST_FAVORITES_ACTIVITY, options.toBundle());
    }

    private void initList() {
        Timber.d("initList: ");
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
        Timber.d("initInputCard: ");
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
        Timber.d("initTranslationCard: ");
        mTranslationContainer = View.inflate(this, R.layout.translation_card_layout, null);
        mTranslationField = ButterKnife.findById(mTranslationContainer, R.id.translate_result);
        mFavoriteStar = ButterKnife.findById(mTranslationContainer, R.id.translate_favorite_star);
    }

    private <T> void addTranslationCard(T item) {
        Timber.d("addTranslationCard: ");
        if (mTranslationContainer != null) {
            mAdapter.removeTranslationCard(mTranslationContainer);
            mAdapter.addTranslationCard(mTranslationContainer);
        }
    }

    private void setDefaultTranslation(TranslationModel model) {
        Timber.d("setDefaultTranslation: ");
        mFromSpinner.setSelection(model.getTranslationDirection().first.ordinal());
        mToSpinner.setSelection(model.getTranslationDirection().second.ordinal());
    }
}
