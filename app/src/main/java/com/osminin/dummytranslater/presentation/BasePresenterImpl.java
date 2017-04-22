package com.osminin.dummytranslater.presentation;

import com.osminin.dummytranslater.presentation.interfaces.base.BasePresenter;
import com.osminin.dummytranslater.ui.base.BaseView;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * TODO: Add a class header comment!
 */

public abstract class BasePresenterImpl<T extends BaseView> implements BasePresenter<T> {

    protected T mView;
    protected CompositeDisposable mDisposable;

    @Override
    public void bind(T view) {
        Timber.d("bind: ");
        mView = view;
    }

    protected void verifyDisposable() {
        Timber.d("verifyDisposable: ");
        if (mDisposable == null || mDisposable.isDisposed()) {
            mDisposable = new CompositeDisposable();
        }
    }
}
