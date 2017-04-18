package com.osminin.dummytranslater.ui.base;

import android.support.v7.app.AppCompatActivity;

import timber.log.Timber;

/**
 * TODO: Add a class header comment!
 */

public class BaseActivity extends AppCompatActivity implements BaseView {
    @Override
    public void showError() {
        Timber.d("showError: ");
    }
}
