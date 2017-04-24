package com.osminin.dummytranslater.db.realm;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.osminin.dummytranslater.models.TranslationModel;

import java.util.Arrays;
import java.util.List;

import io.realm.RealmModel;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by osminin on 3/22/2017.
 */

@RealmClass
public class RealmRecentModel implements RealmModel {

    @Ignore
    private final Gson mGson = new Gson();
    @PrimaryKey
    private String mPrimaryText;
    private String mSecondaryText;
    private String mTranslations;
    private int mTranslateFrom;
    private int mTranslateTo;
    private boolean isFavorite;
    private long mTimestamp;

    public RealmRecentModel() {
    }

    public static RealmRecentModel toDbModel(TranslationModel model) {
        RealmRecentModel realmRecentModel = new RealmRecentModel();
        realmRecentModel.setPrimaryText(model.getPrimaryText());
        realmRecentModel.setSecondaryText(model.getSecondaryText());
        realmRecentModel.setTranslations(model.getTranslations());
        realmRecentModel.setTranslateFrom(model.getTranslationDirection().first.ordinal());
        realmRecentModel.setTranslateTo(model.getTranslationDirection().second.ordinal());
        realmRecentModel.setFavorite(model.isFavorite());
        realmRecentModel.setTimestamp(model.getTimestamp());
        return realmRecentModel;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        mTimestamp = timestamp;
    }

    public String getPrimaryText() {
        return mPrimaryText;
    }

    public void setPrimaryText(String primaryText) {
        mPrimaryText = primaryText;
    }

    public String getSecondaryText() {
        return mSecondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        mSecondaryText = secondaryText;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public List<String> getTranslations() {
        List<String> list = null;
        if (!TextUtils.isEmpty(mTranslations)) {
            list = mGson.fromJson(mTranslations, new TypeToken<List<String>>() {
            }.getType());
        }
        return list;
    }

    public void setTranslations(List<String> translations) {
        mTranslations = mGson.toJson(translations, new TypeToken<List<String>>() {
        }.getType());
    }

    public int getTranslateFrom() {
        return mTranslateFrom;
    }

    public void setTranslateFrom(int translateFrom) {
        mTranslateFrom = translateFrom;
    }

    public int getTranslateTo() {
        return mTranslateTo;
    }

    public void setTranslateTo(int translateTo) {
        mTranslateTo = translateTo;
    }

    public static String getSortField() {
        return "mTimestamp";
    }

    public static String getFavoriteField() {
        return "isFavorite";
    }

    public static String getPrimaryKey() {
        return "mPrimaryText";
    }

    public TranslationModel fromDbModel() {
        return new TranslationModel(getPrimaryText(),
                getSecondaryText(),
                getTranslations(),
                isFavorite(),
                getTranslateFrom(),
                getTranslateTo(),
                getTimestamp());
    }
}
