package com.testableapp.dto;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import java.io.Serializable;

public class User extends BaseObservable implements Serializable {
    private String firstName;
    private final String lastName;
    private final Authentication authentication;

    public User(@NonNull final String firstName, @NonNull final String lastName,
                @NonNull final Authentication authentication) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.authentication = authentication;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

}
