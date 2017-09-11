package com.testableapp.fragments.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import com.testableapp.R;
import com.testableapp.presenters.AbstractPresenter;
import com.testableapp.views.AbstractView;

public abstract class AbstractMvpFragment<P extends AbstractPresenter>
        extends Fragment implements AbstractView {

    private P mPresenter;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();

        onCreateFragment(savedInstanceState, mPresenter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
    }

    public void onCreateFragment(@Nullable final Bundle savedInstanceState,
                                          @NonNull final P presenter) {
        // Nothing to do
    }

    @Override
    public void onViewStateRestored(@Nullable final Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mPresenter.attachView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.detachView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void onNetworkError() {
        Snackbar.make(getView().findViewById(R.id.rootView), "Error de conexión",
                Snackbar.LENGTH_LONG).show();
    }

    @NonNull
    protected abstract P createPresenter();
}
