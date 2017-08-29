package com.testableapp.dto;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Authentication implements Serializable {
    private final String userName;
    private final String password;

    public Authentication(@NonNull final String userName, @NonNull final String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
