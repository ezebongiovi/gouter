package com.testableapp.views;

import android.support.annotation.NonNull;

import com.testableapp.dto.User;

public interface LoginView extends AbstractView {
    void onLogin(@NonNull User user);
}
