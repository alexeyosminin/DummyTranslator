package com.osminin.dummytranslater.ui;

import com.osminin.dummytranslater.models.Languages;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.ui.base.BaseView;

import io.reactivex.Observable;
import io.reactivex.internal.subscriptions.BooleanSubscription;


/**
 * TODO: Add a class header comment!
 */

public interface MainView extends BaseView {
    Observable<Object> textInputObservable();

    Observable<Object> translationDirectionObservable();

    Observable<Languages> fromSpinnerObservable();

    Observable<Languages> toSpinnerObservable();

    Observable<TranslationModel> recentItemsObservable();

    Observable<TranslationModel> activityResultObservable();

    Observable<Object> clearInputObservable();

    Observable<Object> favoriteStarObservable();

    Observable<Integer> optionsMenuObservable();

    <T> Observable<T> changeTransDirection(T item);

    <T> Observable<T> clearRecentList(T item);

    <T> Observable<T> clearInputCard(T item);

    <T> Observable<T> showFavoritesView(T item);

    Observable<Boolean> setFavorite(Boolean isFavorite);

    Observable<TranslationModel> showTranslationView(TranslationModel model);

    Observable<TranslationModel> setPrimaryText(TranslationModel model);

    Observable<TranslationModel> setTranslation(TranslationModel model);

    Observable<TranslationModel> addRecentItem(TranslationModel model);

    Observable<TranslationModel> setDefaultTranslationDirection(TranslationModel model);
}
