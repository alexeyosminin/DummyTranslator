package com.osminin.dummytranslater.db.realm;

import com.osminin.dummytranslater.models.TranslationModel;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * Created by osminin on 3/22/2017.
 */

@RealmClass
public class RealmRecentModel implements RealmModel {

    private String mPrimaryText;
    private String mSecondaryText;
    private boolean isFavorite;

    public RealmRecentModel() {
    }

    public RealmRecentModel(String primaryText, String secondaryText, boolean isFavorite) {
        mPrimaryText = primaryText;
        mSecondaryText = secondaryText;
        this.isFavorite = isFavorite;
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

    public TranslationModel fromDbModel() {
        return new TranslationModel(getPrimaryText(),
                getSecondaryText(),
                isFavorite());
    }

    public static RealmRecentModel toDbModel(Realm realm, TranslationModel model) {
        RealmRecentModel realmRecentModel = realm.createObject(RealmRecentModel.class);
        realmRecentModel.setPrimaryText(model.getPrimaryText());
        realmRecentModel.setSecondaryText(model.getSecondaryText());
        realmRecentModel.setFavorite(model.isFavorite());
        return realmRecentModel;
    }

    public static RealmRecentModel toDbModel(TranslationModel model) {
        return new RealmRecentModel(model.getPrimaryText(),
                model.getSecondaryText(),
                model.isFavorite());
    }
}
