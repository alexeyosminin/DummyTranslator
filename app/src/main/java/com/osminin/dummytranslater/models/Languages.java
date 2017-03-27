package com.osminin.dummytranslater.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * TODO: Add a class header comment!
 */

public enum Languages implements Parcelable {
    AZERBAIJAN("az", 0),
    ALBANIAN("sq", 0),
    AMHARIC("am", 0),
    ENGLISH("en", 0),
    ARABIC("ar", 0),
    ARMENIAN("hy", 0),
    AFRIKAANS("af", 0),
    BASQUE("eu", 0),
    BASHKIR("ba", 0),
    BELARUSIAN("be", 0),
    BENGALI("bn", 0),
    BULGARIAN("bg", 0),
    BOSNIAN("bs", 0),
    WELSH("cy", 0),
    HUNGARIAN("hu", 0),
    VIETNAMESE("vi", 0),
    HAITIAN("ht", 0),
    GALICIAN("gl", 0),
    DUTCH("nl", 0),
    HILL_MARI("mrj", 0),
    GREEK("el", 0),
    GEORGIAN("ka", 0),
    GUJARATI("gu", 0),
    DANISH("da", 0),
    HEBREW("he", 0),
    YIDDISH("yi", 0),
    INDONESIAN("id", 0),
    IRISH("ga", 0),
    ITALIAN("it", 0),
    ICELANDIC("is", 0),
    SPANISH("es", 0),
    KAZAKH("kk", 0),
    KANNADA("kn", 0),
    CATALAN("ca", 0),
    KYRGYZ("ky", 0),
    CHINESE("zh", 0),
    KOREAN("ko", 0),
    XHOSA("xh", 0),
    LATIN("la", 0),
    LATVIAN("lv", 0),
    LITHUANIAN("lt", 0),
    LUXEMBOURGISH("lb", 0),
    MALAGASY("mg", 0),
    MALAY("ms", 0),
    MALAYALAM("ml", 0),
    MALTESE("mt", 0),
    MACEDONIAN("mk", 0),
    MAORI("mi", 0),
    MARATHI("mr", 0),
    MARI("mhr", 0),
    MONGOLIAN("mn", 0),
    GERMAN("de", 0),
    NEPALI("ne", 0),
    NORWEGIAN("no", 0),
    PUNJABI("pa", 0),
    PAPIAMENTO("pap", 0),
    PERSIAN("fa", 0),
    POLISH("pl", 0),
    PORTUGUESE("pt", 0),
    ROMANIAN("ro", 0),
    RUSSIAN("ru", 0),
    CEBUANO("ceb", 0),
    SERBIAN("sr", 0),
    SINHALA("si", 0),
    SLOVAKIAN("sk", 0),
    SLOVENIAN("sl", 0),
    SWAHILI("sw", 0),
    SUNDANESE("su", 0),
    TAJIK("tg", 0),
    THAI("th", 0),
    TAGALOG("tl", 0),
    TAMIL("ta", 0),
    TATAR("tt", 0),
    TELUGU("te", 0),
    TURKISH("tr", 0),
    UDMURT("udm", 0),
    UZBEK("uz", 0),
    UKRAINIAN("uk", 0),
    URDU("ur", 0),
    FINNISH("fi", 0),
    FRENCH("fr", 0),
    HINDI("hi", 0),
    CROATIAN("hr", 0),
    CZECH("cs", 0),
    SWEDISH("sv", 0),
    SCOTTISH("gd", 0),
    ESTONIAN("et", 0),
    ESPERANTO("eo", 0),
    JAVANESE("jv", 0),
    JAPANESE("ja", 0);

    private String mCode;
    private int mHRres;

    Languages(String code, int res) {
        mCode = code;
        mHRres = res;
    }

    public String getCode() {
        return mCode;
    }

    public int getHRres() {
        return mHRres;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Languages> CREATOR = new Parcelable.Creator<Languages>() {
        @Override
        public Languages createFromParcel(Parcel in) {
            return Languages.values()[in.readInt()];
        }

        @Override
        public Languages[] newArray(int size) {
            return new Languages[size];
        }
    };
}
