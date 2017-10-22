package com.testableapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.testableapp.R;
import com.testableapp.manager.AuthenticationManager;
import com.testableapp.presenters.AbstractPresenter;
import com.testableapp.views.AbstractView;

abstract class AbstractMvpActivity<P extends AbstractPresenter>
        extends AppCompatActivity implements AbstractView {
    protected static final int FLAG_BACK_ARROW = 1;
    protected static final int FLAG_NONE = 2;
    protected static final int FLAG_ROOT_VIEW = 4;
    protected static final int FLAG_HIDE_TOOLBAR = 8;

    private final int mFlags;
    private P mPresenter;

    public AbstractMvpActivity() {
        mFlags = FLAG_NONE;
    }

    public AbstractMvpActivity(final int flags) {
        mFlags = flags;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((mFlags & FLAG_ROOT_VIEW) == FLAG_ROOT_VIEW) {
            setContentView(getLayoutResourceId());
        } else {
            setContentView(R.layout.activity_abstract);
            LayoutInflater.from(this).inflate(getLayoutResourceId(),
                    findViewById(R.id.contentLayout));
        }

        if (shouldAuthenticate()) {
            authenticate();
        }

        setUpActionbar();

        mPresenter = createPresenter();

        onCreateActivity(savedInstanceState, mPresenter);
    }

    private void setUpActionbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);

        if (toolbar == null) {
            throw new AssertionError("If using FLAG_ROOT_VIEW you must implement your own toolbar with @+id/toolbar");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        if ((mFlags & FLAG_NONE) == FLAG_NONE) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else if ((mFlags & FLAG_BACK_ARROW) == FLAG_BACK_ARROW) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else if ((mFlags & FLAG_HIDE_TOOLBAR) == FLAG_HIDE_TOOLBAR) {
            getSupportActionBar().hide();
        }
    }

    private void authenticate() {
        // If user is not logged in we redirect to login
        if (AuthenticationManager.getInstance().getUser(this) == null) {
            startActivity(new Intent(this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    protected abstract boolean shouldAuthenticate();

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

    @Override
    public void onError(final String message) {
        Snackbar.make(findViewById(R.id.rootView), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgressLayout() {
        findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.contentLayout).setVisibility(View.GONE);
    }

    @Override
    public void showRegularLayout() {
        findViewById(R.id.progressLayout).setVisibility(View.GONE);
        findViewById(R.id.contentLayout).setVisibility(View.VISIBLE);
    }

    @Override
    public void onGenericError() {
        // TODO: Handle
    }

    public abstract int getLayoutResourceId();

    @NonNull
    protected abstract P createPresenter();
}
