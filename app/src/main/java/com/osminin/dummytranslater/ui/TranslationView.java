package com.osminin.dummytranslater.ui;

import android.view.KeyEvent;

import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.osminin.dummytranslater.models.TranslationModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * TODO: Add a class header comment!
 */

public interface TranslationView {
    Observable<CharSequence> inputTextChanges();
    Observable<KeyEvent> softKeyEvents();
    Observable<Object> crossButtonClicks();

    Observable<TranslationModel> onTextTranslated(TranslationModel item);
    Observable<TranslationModel> onTextInputStop(TranslationModel item);
    <T> Observable<T> showProgress(T item);
    <T> Observable<T> showCrossButton(T item);
    <T> Observable<T> clearInputOutputFields(T item);
}
