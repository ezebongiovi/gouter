package com.testableapp.activities;

import android.support.annotation.NonNull;

import com.testableapp.presenters.EmptyPresenter;
import com.testableapp.views.AbstractView;

public abstract class AbstractActivity extends AbstractMvpActivity<EmptyPresenter>
        implements AbstractView {

    public AbstractActivity(final int flags) {
        super(flags);
    }

    @NonNull
    @Override
    protected EmptyPresenter createPresenter() {
        return new EmptyPresenter();
    }
}
