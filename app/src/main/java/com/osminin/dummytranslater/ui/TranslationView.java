package com.osminin.dummytranslater.ui;

import android.view.KeyEvent;

import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.osminin.dummytranslater.models.Languages;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.ui.base.BaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * TODO: Add a class header comment!
 */

public interface TranslationView extends BaseView{
    Observable<CharSequence> inputTextChanges();
    Observable<KeyEvent> softEnterKeyEvents();
    Observable<Object> crossButtonClicks();
    Observable<Object> backButtonClicks();

    void setTranslationHintFrom(Languages language);
    void setTranslationHintTo(Languages language);

    Observable<TranslationModel> onTextTranslated(TranslationModel item);
    Observable<TranslationModel> onTextInputStop(TranslationModel item);
    <T> Observable<T> showProgress(T item);
    <T> Observable<T> hideProgress(T item);
    <T> Observable<T> hideKeyboard(T item);
    <T> Observable<T> showCrossButton(T item);
    <T> Observable<T> clearInputOutputFields(T item);
}
