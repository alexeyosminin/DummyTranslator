package com.osminin.dummytranslater.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by osminin on 3/21/2017.
 */

public class TranslationModel implements Parcelable {
    private String mPrimaryText;
    private String mSecondaryText;
    private boolean isFavorite;

    public TranslationModel() {
    }

    public TranslationModel(String primaryText, String secondaryText, boolean isFavorite) {
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

    protected TranslationModel(Parcel in) {
        mPrimaryText = in.readString();
        mSecondaryText = in.readString();
        isFavorite = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPrimaryText);
        dest.writeString(mSecondaryText);
        dest.writeByte((byte) (isFavorite ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TranslationModel> CREATOR = new Parcelable.Creator<TranslationModel>() {
        @Override
        public TranslationModel createFromParcel(Parcel in) {
            return new TranslationModel(in);
        }

        @Override
        public TranslationModel[] newArray(int size) {
            return new TranslationModel[size];
        }
    };
}