package com.osminin.dummytranslater.models;

/**
 * Created by osminin on 3/21/2017.
 */

public class RecentModel {
    private String mPrimaryText;
    private String mSecondaryText;
    private boolean isFavorite;

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
}
