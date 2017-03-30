package com.osminin.dummytranslater.ui;

import com.jakewharton.rxbinding2.internal.Notification;
import com.osminin.dummytranslater.models.Languages;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.ui.base.BaseView;

import io.reactivex.Observable;


/**
 * TODO: Add a class header comment!
 */

public interface MainView extends BaseView {
    void onTextTranslated(String text);

    Observable<Object> textInputObservable();
    Observable<Object> changeTranslationDirectionClicks();
    Observable<Languages> fromSpinnerObservable();
    Observable<Languages> toSpinnerObservable();
    Observable<TranslationModel> getRecentItemsClicks();

    <T> Observable<T> changeTransDirection(T item);



    Observable<TranslationModel> showTranslationView(TranslationModel model);
}
