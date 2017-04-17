package com.osminin.dummytranslater.ui;

import android.os.Bundle;

import com.osminin.dummytranslater.R;
import com.osminin.dummytranslater.presentation.interfaces.FavoritesPresenter;
import com.osminin.dummytranslater.ui.base.BaseActivity;

import javax.inject.Inject;

/**
 * Created by osminin on 4/17/2017.
 */

public final class FavoritesActivity extends BaseActivity implements FavoritesView {

    @Inject
    FavoritesPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
    }
}
