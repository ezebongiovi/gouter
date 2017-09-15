package com.testableapp.dto;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

public class User extends BaseObservable implements Serializable {
    private String _id;
    private String firstName;
    private final String lastName;
    private final Authentication authentication;
    private final String profilePicture;

    public User(final String id, @NonNull final String firstName, @NonNull final String lastName,
                @Nullable final String profilePicture,
                @NonNull final Authentication authentication) {
        this._id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.authentication = authentication;
        this.profilePicture = profilePicture;
    }

    public String getId() {
        return _id;
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

    public String getProfilePicture() {
        return profilePicture;
    }
}
