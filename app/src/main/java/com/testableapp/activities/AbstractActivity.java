package com.testableapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.testableapp.R;
import com.testableapp.presenters.AbstractPresenter;
import com.testableapp.views.AbstractView;

abstract class AbstractActivity<P extends AbstractPresenter>
        extends AppCompatActivity implements AbstractView {

    private P mPresenter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract);

        LayoutInflater.from(this).inflate(getLayoutResourceId(),
                (ViewGroup) findViewById(R.id.rootView));

        mPresenter = createPresenter();

        onCreateActivity(savedInstanceState, mPresenter);
    }

    public abstract void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                          @NonNull final P presenter);

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mPresenter.attachView(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mPresenter.detachView();
    }

    @Override
    public void onNetworkError() {
        Snackbar.make(findViewById(R.id.rootView), "Error de conexión",
                Snackbar.LENGTH_LONG).show();
    }

    public abstract int getLayoutResourceId();

    @NonNull
    protected abstract P createPresenter();
}
