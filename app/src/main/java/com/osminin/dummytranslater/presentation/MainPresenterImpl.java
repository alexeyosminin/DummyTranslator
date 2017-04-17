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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.osminin.dummytranslater.Config.MAX_RECENTS_COUNT;

/**
 * TODO: Add a class header comment!
 */

public final class MainPresenterImpl implements MainPresenter {

    @Inject
    TranslationDataStore mDataStore;

    private MainView mView;
    private TranslationModel mTranslationModel;
    private CompositeDisposable mDisposable;

    @Override
    public void bind(MainView view) {
        mView = view;
        mTranslationModel = new TranslationModel();
        App.plusDbComponent().inject(this);
    }

    @Override
    public void startObserveUiEvents() {
        verifyDisposable();
        mDisposable.add(mView.textInputObservable()
                .map(o -> mTranslationModel)
                .switchMap(mView::showTranslationView)
                .switchMap(mView::clearRecentList)
                .subscribe());

        mDisposable.add(mView.translationDirectionObservable()
                .switchMap(mView::changeTransDirection)
                .subscribe());

        mDisposable.add(mView.fromSpinnerObservable()
                .subscribe(l -> mTranslationModel.setTranslationFrom(l)));

        mDisposable.add(mView.toSpinnerObservable()
                .subscribe(l -> mTranslationModel.setTranslationTo(l)));

        mDisposable.add(mView.activityResultObservable()
                .doOnNext(model -> mTranslationModel = model)
                .switchMap(mView::setPrimaryText)
                .filter(m -> m.getTranslations() != null
                        && !m.getTranslations().isEmpty())
                .switchMap(mView::setTranslation)
                .doOnError(m -> mView.showError())
                .subscribe());
        mDisposable.add(loadRecent()
                .subscribeOn(Schedulers.single())
                .first(getDefaultModel())
                .toObservable()
                .switchMap(mView::setDefaultTranslationDirection)
                .doOnComplete(() -> reloadRecents())
                .subscribe());
        mDisposable.add(mView.clearInputObservable()
                .switchMap(mView::clearInputCard)
                .doOnNext(o -> clearTranslationModel())
                .subscribe());
        mDisposable.add(mView.recentItemsObservable()
                .doOnNext(model -> {
                    mTranslationModel = model;
                })
                .switchMap(mView::setDefaultTranslationDirection)
                .switchMap(mView::setPrimaryText)
                .switchMap(mView::setTranslation)
                .subscribe());
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
                .switchMap(mView::clearRecentList)
                .doOnNext(translationModel -> reloadRecents())
                .subscribe());
        mDisposable.add(mView.optionsMenuObservable()
                .switchMap(this::handleOptionsMenuClick)
                .subscribe());
    }

    @Override
    public void stopObserveUiEvents() {
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    private Observable<TranslationModel> loadRecent() {
        return mDataStore.open(mDataStore)
                .switchMap(dataStore -> dataStore.queryAll())
                .doOnComplete(() -> mDisposable.add(mDataStore.close(mDataStore).subscribe()))
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void verifyDisposable() {
        if (mDisposable == null || mDisposable.isDisposed()) {
            mDisposable = new CompositeDisposable();
        }
    }

    private <T> TranslationModel getDefaultModel() {
        TranslationModel model = new TranslationModel();
        model.setTranslationDirection(Languages.ENGLISH, Languages.RUSSIAN);
        return model;
    }

    private void clearTranslationModel() {
        mTranslationModel.setPrimaryText(null);
        mTranslationModel.setTranslations(null);
    }

    private void reloadRecents() {
        mDisposable.add(loadRecent()
                .subscribeOn(Schedulers.single())
                .take(MAX_RECENTS_COUNT)
                .switchMap(mView::addRecentItem)
                .subscribe());
    }

    private Observable<Integer> handleOptionsMenuClick(Integer item) {
        switch (item) {
            case R.id.favorites:
                return mView.showFavoritesView(item);
        }
        return Observable.empty();
    }
}
