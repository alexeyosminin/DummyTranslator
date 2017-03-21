package com.osminin.dummytranslater.presentation.interfaces;

import com.osminin.dummytranslater.models.RecentModel;
import com.osminin.dummytranslater.presentation.interfaces.base.BasePresenter;
import com.osminin.dummytranslater.ui.MainView;

import io.reactivex.Observable;

/**
 * TODO: Add a class header comment!
 */

public interface MainPresenter extends BasePresenter<MainView> {
    void startObserveTextInput(Observable<Object> observable);
    void startObserveRecentClicks(Observable<RecentModel> observable);
    void stopObserveUiEvents();
}
