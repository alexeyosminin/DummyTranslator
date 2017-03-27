package com.osminin.dummytranslater.ui;

import com.jakewharton.rxbinding2.internal.Notification;
import com.osminin.dummytranslater.ui.base.BaseView;

import io.reactivex.Observable;


/**
 * TODO: Add a class header comment!
 */

public interface MainView extends BaseView {
    void onTextTranslated(String text);

    Observable<Object> textInputObservable();

    void showTranslationView();
}
