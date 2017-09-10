package com.testableapp.views;

import android.support.annotation.NonNull;

import com.testableapp.dto.User;

public interface RegisterView extends AbstractView {
    void onInvalidData();

    void onRegister(@NonNull User user);
}
