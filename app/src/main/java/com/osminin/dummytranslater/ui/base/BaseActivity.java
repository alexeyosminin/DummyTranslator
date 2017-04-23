package com.osminin.dummytranslater.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.osminin.dummytranslater.R;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * TODO: Add a class header comment!
 */

public class BaseActivity extends AppCompatActivity implements BaseView {

    @Override
    public void showError() {
        Timber.d("showError: ");
        Snackbar.make(ButterKnife.findById(this, R.id.base_layout), R.string.snackbar_text, Snackbar.LENGTH_LONG)
                .setDuration(3000).show();
    }
}
