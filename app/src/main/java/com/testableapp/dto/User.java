package com.testableapp.dto;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class User extends BaseObservable implements Parcelable {

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

    protected User(final Parcel in) {
        _id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        authentication = in.readParcelable(Authentication.class.getClassLoader());
        profilePicture = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(final Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(final int size) {
            return new User[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeParcelable(authentication, flags);
        dest.writeString(profilePicture);
    }
}
