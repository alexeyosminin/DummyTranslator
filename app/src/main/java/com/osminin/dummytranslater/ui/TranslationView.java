package com.osminin.dummytranslater.ui;

import android.view.KeyEvent;

import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.osminin.dummytranslater.models.TranslationModel;

import java.util.List;

/**
 * TODO: Add a class header comment!
 */

public interface TranslationView {
    void onTextTranslated(List<String> text);
    void onTextInputStop(TranslationModel model);
}
