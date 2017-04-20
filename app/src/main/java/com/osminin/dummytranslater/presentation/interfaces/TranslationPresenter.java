package com.osminin.dummytranslater.presentation.interfaces;

import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.presentation.interfaces.base.BasePresenter;
import com.osminin.dummytranslater.ui.TranslationView;

/**
 * TODO: Add a class header comment!
 */

public interface TranslationPresenter extends BasePresenter<TranslationView> {
    void startObserveUiChanges();

    void stopObserveUiChanges();

    void setTranslationModel(TranslationModel model);
}
