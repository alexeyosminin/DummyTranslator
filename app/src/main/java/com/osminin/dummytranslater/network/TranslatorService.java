package com.osminin.dummytranslater.network;

import android.util.Pair;

import com.osminin.dummytranslater.models.Languages;
import com.osminin.dummytranslater.models.TranslationModel;

import io.reactivex.Observable;

/**
 * Created by osminin on 3/17/2017.
 */

public interface TranslatorService {
    Observable<TranslationModel> translate(Pair<Languages, Languages> translationDirection,
                                           String text);
}
