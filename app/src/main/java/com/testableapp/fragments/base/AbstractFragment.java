package com.testableapp.fragments.base;

import android.support.annotation.NonNull;

import com.testableapp.presenters.EmptyPresenter;
import com.testableapp.views.AbstractView;

public abstract class AbstractFragment extends AbstractMvpFragment<EmptyPresenter>
        implements AbstractView {

    @NonNull
    @Override
    protected EmptyPresenter createPresenter() {
        return new EmptyPresenter();
    }
}
