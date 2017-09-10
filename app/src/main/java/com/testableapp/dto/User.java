package com.testableapp.dto;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import java.io.Serializable;

public class User extends BaseObservable implements Serializable {
    private String name;
    private final String lastName;
    private final Authentication authentication;

    public User(@NonNull final String name, @NonNull final String lastName,
                @NonNull final Authentication authentication) {
        this.name = name;
        this.lastName = lastName;
        this.authentication = authentication;
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

}
