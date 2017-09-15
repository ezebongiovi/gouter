package com.testableapp.views;

import android.support.annotation.NonNull;

public interface AbstractView {
    void onNetworkError();

    void onError(@NonNull String message);

    void showProgressLayout();

    void showRegularLayout();

    void onGenericError();
}
