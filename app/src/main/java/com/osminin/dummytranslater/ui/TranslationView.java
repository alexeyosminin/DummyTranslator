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

    Observable<TranslationModel> onTextTranslated(TranslationModel model);
    Observable<TranslationModel> onTextInputStop(TranslationModel model);
}
