package com.osminin.dummytranslater.presentation.interfaces;

import com.osminin.dummytranslater.presentation.interfaces.base.BasePresenter;
import com.osminin.dummytranslater.ui.MainView;

import io.reactivex.Observable;

/**
 * TODO: Add a class header comment!
 */

public interface MainPresenter extends BasePresenter<MainView> {
    void startObserveTextChanges(Observable<CharSequence> observable);
    void stopObserveTextChanges();
}
