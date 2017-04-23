package com.osminin.dummytranslater.presentation.interfaces;

import com.osminin.dummytranslater.presentation.interfaces.base.BasePresenter;
import com.osminin.dummytranslater.ui.FavoritesView;

/**
 * Created by osminin on 4/17/2017.
 */

public interface FavoritesPresenter extends BasePresenter<FavoritesView> {
    void startObserveUiEvents();

    void stopObserveUiEvents();
}
