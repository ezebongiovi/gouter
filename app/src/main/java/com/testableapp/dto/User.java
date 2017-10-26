package com.testableapp.dto;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class User extends BaseObservable implements Parcelable {

    public final String _id;
    public final String firstName;
    public final String lastName;
    public final Authentication authentication;
    public final Image profilePicture;
    public final Country country;

    public User(final String id, @NonNull final String firstName, @NonNull final String lastName,
                @Nullable final Image profilePicture,
                @NonNull final Authentication authentication, @NonNull final Country country) {
        this._id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.authentication = authentication;
        this.profilePicture = profilePicture;
        this.country = country;
    }

    protected User(final Parcel in) {
        _id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        authentication = in.readParcelable(Authentication.class.getClassLoader());
        profilePicture = in.readParcelable(Image.class.getClassLoader());
        country = in.readParcelable(Country.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(_id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeParcelable(authentication, flags);
        dest.writeParcelable(profilePicture, flags);
        dest.writeParcelable(country, flags);
    }
}
