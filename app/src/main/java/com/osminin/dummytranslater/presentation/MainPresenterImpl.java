package com.osminin.dummytranslater.presentation;

import com.osminin.dummytranslater.R;
import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.db.TranslationDataStore;
import com.osminin.dummytranslater.models.Languages;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.presentation.interfaces.MainPresenter;
import com.osminin.dummytranslater.ui.MainView;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.osminin.dummytranslater.Config.MAX_RECENTS_COUNT;

/**
 * TODO: Add a class header comment!
 */

public final class MainPresenterImpl extends BasePresenterImpl<MainView> implements MainPresenter {

    @Inject
    TranslationDataStore mDataStore;

    private TranslationModel mTranslationModel;

    @Override
    public void bind(MainView view) {
        super.bind(view);
        mTranslationModel = new TranslationModel();
        App.plusDbComponent().inject(this);
    }

    @Override
    public void destroy() {
        Timber.d("destroy: ");
        App.clearDbComponent();
    }

    @Override
    public void startObserveUiEvents() {
        Timber.d("startObserveUiEvents: ");
        prepareDisposable();
        //start listen text input
        addTextInputObservable();
        //start listen translation direction changes
        addTranslationDirectionObservables();
        //start listen activity result callback
        addActivityResultObservable();
        // load recent items
        mDisposable.add(loadRecent()
                .subscribeOn(Schedulers.single())
                .first(getDefaultModel())
                .toObservable()
                .switchMap(mView::setDefaultTranslationDirection)
                .doOnComplete(() -> reloadRecents())
                .subscribe());
        //start observe clear input btn
        addClearInputObservable();
        // start observe recent item clicks
        addRecentClicksObservable();
        // listen for favorite clicks
        addFavoriteClickObservable();
        // start observe options menu clicks
        addOptionsMenuObservable();
    }

    @Override
    public void stopObserveUiEvents() {
        Timber.d("stopObserveUiEvents: ");
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    private void addTextInputObservable() {
        Timber.v("addTextInputObservable: ");
        mDisposable.add(mView.textInputObservable()
                .map(o -> mTranslationModel)
                .switchMap(mView::showTranslationView)
                .switchMap(mView::clearRecentList)
                .subscribe());
    }

    private void addTranslationDirectionObservables() {
        Timber.v("addTranslationDirectionObservables: ");
        mDisposable.add(mView.translationDirectionObservable()
                .switchMap(mView::changeTransDirection)
                .subscribe());

        mDisposable.add(mView.fromSpinnerObservable()
                .subscribe(l -> mTranslationModel.setTranslationFrom(l)));

        mDisposable.add(mView.toSpinnerObservable()
                .subscribe(l -> mTranslationModel.setTranslationTo(l)));
    }

    private void addActivityResultObservable() {
        Timber.v("addActivityResultObservable: ");
        mDisposable.add(mView.activityResultObservable()
                .switchMap(mView::requestInputFocus)
                .filter(m -> m.getTranslations() != null
                        && !m.getTranslations().isEmpty())
                .doOnNext(model -> mTranslationModel = model.clone())
                .switchMap(mView::setPrimaryText)
                .switchMap(mView::setTranslation)
                .subscribe());
    }

    private void addClearInputObservable() {
        Timber.v("addClearInputObservable: ");
        mDisposable.add(mView.clearInputObservable()
                .switchMap(mView::clearInputCard)
                .doOnNext(o -> clearTranslationModel())
                .subscribe());
    }

    private void addRecentClicksObservable() {
        Timber.v("addRecentClicksObservable: ");
        mDisposable.add(mView.recentItemsObservable()
                .doOnNext(model -> {
                    mTranslationModel = model.clone();
                })
                .switchMap(mView::setDefaultTranslationDirection)
                .switchMap(mView::setPrimaryText)
                .switchMap(mView::setTranslation)
                .subscribe());
    }

    private void addFavoriteClickObservable() {
        Timber.v("addFavoriteClickObservable: ");
        mDisposable.add(mView.favoriteStarObservable()
                .map(o -> !mTranslationModel.isFavorite())
                .doOnNext(isFav -> {
                    mTranslationModel.setFavorite(isFav);
                })
                .switchMap(mView::setFavorite)
                .map(f -> mTranslationModel)
                .observeOn(Schedulers.single())
                .switchMap(mDataStore::open)
                .switchMap(mDataStore::update)
                .switchMap(mDataStore::close)
                .observeOn(AndroidSchedulers.mainThread())
                .switchMap(mView::updateRecentItem)
                .subscribe());
    }

    private void addOptionsMenuObservable() {
        Timber.v("addOptionsMenuObservable: ");
        mDisposable.add(mView.optionsMenuObservable()
                .switchMap(this::handleOptionsMenuClick)
                .subscribe());
    }

    private Observable<TranslationModel> loadRecent() {
        Timber.d("loadRecent: ");
        return mDataStore.open(mDataStore)
                .switchMap(dataStore -> dataStore.queryAll())
                .doOnComplete(() -> mDisposable.add(mDataStore.close(mDataStore).subscribe()))
                .observeOn(AndroidSchedulers.mainThread());
    }

    private <T> TranslationModel getDefaultModel() {
        Timber.d("getDefaultModel: ");
        TranslationModel model = new TranslationModel();
        model.setTranslationDirection(Languages.ENGLISH, Languages.RUSSIAN);
        return model;
    }

    private void clearTranslationModel() {
        Timber.d("clearTranslationModel: %s", mTranslationModel);
        mTranslationModel.setPrimaryText(null);
        mTranslationModel.setTranslations(null);
    }

    private void reloadRecents() {
        Timber.d("reloadRecents: ");
        mDisposable.add(loadRecent()
                .subscribeOn(Schedulers.single())
                .take(MAX_RECENTS_COUNT)
                .switchMap(mView::addRecentItem)
                .subscribe());
    }

    private Observable<Integer> handleOptionsMenuClick(Integer item) {
        Timber.d("handleOptionsMenuClick: ");
        switch (item) {
            case R.id.favorites:
                return mView.showFavoritesView(item);
        }
        return Observable.empty();
    }
}
