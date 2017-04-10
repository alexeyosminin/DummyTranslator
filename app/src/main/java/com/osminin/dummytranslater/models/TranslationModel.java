package com.osminin.dummytranslater.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by osminin on 3/21/2017.
 */

public class TranslationModel implements Parcelable {
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
    private String mPrimaryText;
    private String mSecondaryText;
    private List<String> mTranslations;
    private boolean isFavorite;
    private Pair<Languages, Languages> mTranslationDirection;
    private long mTimestamp;

    public TranslationModel(String primaryText, String secondaryText, List<String> translations, boolean isFavorite) {
        mPrimaryText = primaryText;
        mSecondaryText = secondaryText;
        mTranslations = translations;
        this.isFavorite = isFavorite;
        mTimestamp = System.currentTimeMillis();
    }

    public TranslationModel() {
        mTimestamp = System.currentTimeMillis();
    }

    public TranslationModel(String primaryText, String secondaryText, boolean isFavorite) {
        mPrimaryText = primaryText;
        mSecondaryText = secondaryText;
        this.isFavorite = isFavorite;
    }

    public TranslationModel(String primaryText, String secondaryText, List<String> translations,
                            boolean isFavorite, int from, int to, long timestamp) {
        mPrimaryText = primaryText;
        mSecondaryText = secondaryText;
        mTranslations = translations;
        this.isFavorite = isFavorite;
        setTranslationFrom(Languages.values()[from]);
        setTranslationTo(Languages.values()[to]);
        mTimestamp = timestamp;
    }

    protected TranslationModel(Parcel in) {
        mPrimaryText = in.readString();
        mSecondaryText = in.readString();
        if (in.readByte() == 0x01) {
            mTranslations = new ArrayList<>();
            in.readList(mTranslations, String.class.getClassLoader());
        } else {
            mTranslations = null;
        }
        isFavorite = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            Languages from = in.readParcelable(Languages.class.getClassLoader());
            Languages to = in.readParcelable(Languages.class.getClassLoader());
            mTranslationDirection = new Pair<>(from, to);
        } else {
            mTranslationDirection = null;
        }
        mTimestamp = in.readLong();
    }

    public Pair<Languages, Languages> getTranslationDirection() {
        return mTranslationDirection;
    }

    public void setTranslationDirection(Languages from, Languages to) {
        mTranslationDirection = new Pair<>(from, to);
    }

    public void setTranslationDirection(Pair<Languages, Languages> direction) {
        mTranslationDirection = direction;
    }

    public void setTranslationFrom(Languages from) {
        if (mTranslationDirection == null) {
            mTranslationDirection = new Pair<>(from, null);
        } else {
            mTranslationDirection = new Pair<>(from, mTranslationDirection.second);
        }
    }

    public void setTranslationTo(Languages to) {
        if (mTranslationDirection == null) {
            mTranslationDirection = new Pair<>(null, to);
        } else {
            mTranslationDirection = new Pair<>(mTranslationDirection.first, to);
        }
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
        return mTranslations;
    }

    public void setTranslations(List<String> translations) {
        mTranslations = translations;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        mTimestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPrimaryText);
        dest.writeString(mSecondaryText);
        if (mTranslations == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mTranslations);
        }
        dest.writeByte((byte) (isFavorite ? 0x01 : 0x00));
        if (mTranslationDirection == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeParcelable(mTranslationDirection.first, flags);
            dest.writeParcelable(mTranslationDirection.second, flags);
        }
        dest.writeLong(mTimestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TranslationModel that = (TranslationModel) o;

        if (!mPrimaryText.equals(that.mPrimaryText)) return false;
        return mTranslationDirection.equals(that.mTranslationDirection);

    }

    @Override
    public int hashCode() {
        int result = mPrimaryText.hashCode();
        result = 31 * result + mTranslationDirection.hashCode();
        return result;
    }
}