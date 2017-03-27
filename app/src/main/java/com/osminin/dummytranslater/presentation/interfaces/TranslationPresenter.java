package com.osminin.dummytranslater.presentation.interfaces;

import android.util.Pair;
import android.view.KeyEvent;

import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.osminin.dummytranslater.models.Languages;
import com.osminin.dummytranslater.presentation.interfaces.base.BasePresenter;
import com.osminin.dummytranslater.ui.TranslationView;

import io.reactivex.Observable;

/**
 * TODO: Add a class header comment!
 */

public interface TranslationPresenter extends BasePresenter<TranslationView> {
    void startObserveUiChanges();
    void stopObserveUiChanges();

    void setTranslationDirection(Pair<Languages, Languages> direction);
    void destroy();
}
