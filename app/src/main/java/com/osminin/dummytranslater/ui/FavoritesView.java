package com.osminin.dummytranslater.ui;

import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.ui.base.BaseView;

/**
 * Created by osminin on 4/17/2017.
 */

public interface FavoritesView extends BaseView {
    void addItem(TranslationModel model);

    void clearList();
}
