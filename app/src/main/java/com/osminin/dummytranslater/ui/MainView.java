package com.osminin.dummytranslater.ui;

import com.osminin.dummytranslater.ui.base.BaseView;

import io.reactivex.Observable;


/**
 * TODO: Add a class header comment!
 */

public interface MainView extends BaseView {
    void onTextTranslated(String text);

    void showTranslationView();
}
