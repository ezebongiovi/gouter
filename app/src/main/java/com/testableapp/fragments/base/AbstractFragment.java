package com.testableapp.fragments.base;

import android.support.annotation.NonNull;

import com.testableapp.presenters.EmptyPresenter;
import com.testableapp.views.AbstractView;

public abstract class AbstractFragment extends AbstractMvpFragment<EmptyPresenter>
        implements AbstractView {

    @Override
    public void onGenericError() {
        // TODO: Handle generic error
    }

    @Override
    public void showProgressLayout() {
        // Nothing to do
    }

    @Override
    public void showRegularLayout() {
        // Nothing to do
    }

    @NonNull
    @Override
    protected EmptyPresenter createPresenter() {
        return new EmptyPresenter();
    }
}
