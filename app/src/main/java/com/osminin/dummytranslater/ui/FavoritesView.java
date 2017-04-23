package com.osminin.dummytranslater.ui;

import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.ui.base.BaseView;

import io.reactivex.Observable;


/**
 * Created by osminin on 4/17/2017.
 */

public interface FavoritesView extends BaseView {

    Observable<TranslationModel> itemClickObservable();

    void finishView(TranslationModel model);

    void addItem(TranslationModel model);

    void clearList();
}
