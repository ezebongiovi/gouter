package com.testableapp.dto;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {
    private final String name;
    private final String lastName;
    private final Authentication authentication;

    public User(@NonNull final String name, @NonNull final String lastName,
                @NonNull final Authentication authentication) {
        this.name = name;
        this.lastName = lastName;
        this.authentication = authentication;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public Authentication getAuthentication() {
        return authentication;
    }
}
