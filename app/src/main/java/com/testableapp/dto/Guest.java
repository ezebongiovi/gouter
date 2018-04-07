package com.testableapp.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Guest implements Parcelable {
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_REJECTED = "rejected";
    public static final String STATUS_ACCEPTED = "ok";

    protected Guest(final Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        status = in.readString();
    }

    public static final Creator<Guest> CREATOR = new Creator<Guest>() {
        @Override
        public Guest createFromParcel(final Parcel in) {
            return new Guest(in);
        }

        @Override
        public Guest[] newArray(final int size) {
            return new Guest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user, flags);
        dest.writeString(status);
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({STATUS_ACCEPTED, STATUS_PENDING, STATUS_REJECTED})
    public @interface Status {

    }

    public final User user;
    public final String status;

    /**
     * Default constructor
     *
     * @param user   the user's information
     * @param status the invitation status
     */
    public Guest(@NonNull final User user, @NonNull @Status final String status) {
        this.user = user;
        this.status = status;
    }
}
