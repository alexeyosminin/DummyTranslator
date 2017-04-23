package com.osminin.dummytranslater.presentation.interfaces.base;

import com.osminin.dummytranslater.ui.base.BaseView;

public interface BasePresenter<T extends BaseView> {
    void bind(T view);

    void destroy();
}
