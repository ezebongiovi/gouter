package com.testableapp.dto;

import android.support.annotation.NonNull;

public class RegistrationRequest {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;

    public RegistrationRequest(@NonNull final String firstName, @NonNull final String lastName,
                               @NonNull final String email, @NonNull final String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
