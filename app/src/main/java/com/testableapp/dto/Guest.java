package com.testableapp.dto;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Guest {
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_REJECTED = "rejected";
    public static final String STATUS_ACCEPTED = "accepted";

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
