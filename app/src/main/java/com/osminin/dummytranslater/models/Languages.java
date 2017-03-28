package com.osminin.dummytranslater.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.osminin.dummytranslater.R;

/**
 * TODO: Add a class header comment!
 */

public enum Languages implements Parcelable {
    AZERBAIJAN("az", R.string.language_azerbaijan),
    ALBANIAN("sq", R.string.language_albanian),
    AMHARIC("am", R.string.language_amharic),
    ENGLISH("en", R.string.language_english),
    ARABIC("ar", R.string.language_arabic),
    ARMENIAN("hy", R.string.language_armenian),
    AFRIKAANS("af", R.string.language_afrikaans),
    BASQUE("eu", R.string.language_basque),
    BASHKIR("ba", R.string.language_bashkir),
    BELARUSIAN("be", R.string.language_belarusian),
    BENGALI("bn", R.string.language_bengali),
    BULGARIAN("bg", R.string.language_bulgarian),
    BOSNIAN("bs", R.string.language_bosnian),
    WELSH("cy", R.string.language_welsh),
    HUNGARIAN("hu", R.string.language_hungarian),
    VIETNAMESE("vi", R.string.language_vietnamese),
    HAITIAN("ht", R.string.language_haitian),
    GALICIAN("gl", R.string.language_galician),
    DUTCH("nl", R.string.language_dutch),
    HILL_MARI("mrj", R.string.language_hill_mari),
    GREEK("el", R.string.language_greek),
    GEORGIAN("ka", R.string.language_georgian),
    GUJARATI("gu", R.string.language_gujarati),
    DANISH("da", R.string.language_danish),
    HEBREW("he", R.string.language_hebrew),
    YIDDISH("yi", R.string.language_yiddish),
    INDONESIAN("id", R.string.language_indonesian),
    IRISH("ga", R.string.language_irish),
    ITALIAN("it", R.string.language_italian),
    ICELANDIC("is", R.string.language_icelandic),
    SPANISH("es", R.string.language_spanish),
    KAZAKH("kk", R.string.language_kazakh),
    KANNADA("kn", R.string.language_kannada),
    CATALAN("ca", R.string.language_catalan),
    KYRGYZ("ky", R.string.language_kyrgyz),
    CHINESE("zh", R.string.language_chinese),
    KOREAN("ko", R.string.language_korean),
    XHOSA("xh", R.string.language_xhosa),
    LATIN("la", R.string.language_latin),
    LATVIAN("lv", R.string.language_latvian),
    LITHUANIAN("lt", R.string.language_lithuanian),
    LUXEMBOURGISH("lb", R.string.language_luxembourgish),
    MALAGASY("mg", R.string.language_malagasy),
    MALAY("ms", R.string.language_malay),
    MALAYALAM("ml", R.string.language_malayalam),
    MALTESE("mt", R.string.language_maltese),
    MACEDONIAN("mk", R.string.language_macedonian),
    MAORI("mi", R.string.language_maori),
    MARATHI("mr", R.string.language_marathi),
    MARI("mhr", R.string.language_mari),
    MONGOLIAN("mn", R.string.language_mongolian),
    GERMAN("de", R.string.language_german),
    NEPALI("ne", R.string.language_nepali),
    NORWEGIAN("no", R.string.language_norwegian),
    PUNJABI("pa", R.string.language_punjabi),
    PAPIAMENTO("pap", R.string.language_papiamento),
    PERSIAN("fa", R.string.language_persian),
    POLISH("pl", R.string.language_polish),
    PORTUGUESE("pt", R.string.language_portuguese),
    ROMANIAN("ro", R.string.language_romanian),
    RUSSIAN("ru", R.string.language_russian),
    CEBUANO("ceb", R.string.language_cebuano),
    SERBIAN("sr", R.string.language_serbian),
    SINHALA("si", R.string.language_sinhala),
    SLOVAKIAN("sk", R.string.language_slovakian),
    SLOVENIAN("sl", R.string.language_slovenian),
    SWAHILI("sw", R.string.language_swahili),
    SUNDANESE("su", R.string.language_sundanese),
    TAJIK("tg", R.string.language_tajik),
    THAI("th", R.string.language_thai),
    TAGALOG("tl", R.string.language_tagalog),
    TAMIL("ta", R.string.language_tamil),
    TATAR("tt", R.string.language_tatar),
    TELUGU("te", R.string.language_telugu),
    TURKISH("tr", R.string.language_turkish),
    UDMURT("udm", R.string.language_udmurt),
    UZBEK("uz", R.string.language_uzbek),
    UKRAINIAN("uk", R.string.language_ukrainian),
    URDU("ur", R.string.language_urdu),
    FINNISH("fi", R.string.language_finnish),
    FRENCH("fr", R.string.language_french),
    HINDI("hi", R.string.language_hindi),
    CROATIAN("hr", R.string.language_croatian),
    CZECH("cs", R.string.language_czech),
    SWEDISH("sv", R.string.language_swedish),
    SCOTTISH("gd", R.string.language_scottish),
    ESTONIAN("et", R.string.language_estonian),
    ESPERANTO("eo", R.string.language_esperanto),
    JAVANESE("jv", R.string.language_javanese),
    JAPANESE("ja", R.string.language_japanese);

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

    public static String[] getHRStrings(Context context) {
        String[] res = new String[Languages.values().length];
        int i = 0;
        for (Languages lang : Languages.values()) {
            res[i++] = context.getString(lang.getHRres());
        }
        return res;
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
