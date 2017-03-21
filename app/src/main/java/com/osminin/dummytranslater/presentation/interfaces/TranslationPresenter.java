package com.osminin.dummytranslater.presentation.interfaces;

import com.osminin.dummytranslater.presentation.interfaces.base.BasePresenter;
import com.osminin.dummytranslater.ui.TranslationView;

import io.reactivex.Observable;

/**
 * TODO: Add a class header comment!
 */

public interface TranslationPresenter extends BasePresenter<TranslationView> {
    void startObserveTextChanges(Observable<CharSequence> observable);
    void stopObserveTextChanges();
    void destroy();
}
