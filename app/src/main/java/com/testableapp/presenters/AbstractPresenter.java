package com.testableapp.presenters;

import android.support.annotation.NonNull;

import com.testableapp.views.AbstractView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class AbstractPresenter<V extends AbstractView> {

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private V mView;

    public void attachView(@NonNull final V view) {
        mView = view;
    }

    protected V getView() {
        return mView;
    }

    protected void addDisposable(@NonNull final Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    public void detachView() {
        mCompositeDisposable.clear();
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }
}
