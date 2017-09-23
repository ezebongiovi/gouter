package com.testableapp.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Authentication implements Parcelable {

    private final String email;
    private final String password;
    private final String accessToken;

    public Authentication(@NonNull final String email, @NonNull final String password) {
        this.email = email;
        this.password = password;
        this.accessToken = null;
    }

    private Authentication(final Builder builder) {
        this.accessToken = builder.accessToken;
        this.email = builder.email;
        this.password = builder.password;
    }

    protected Authentication(final Parcel in) {
        email = in.readString();
        password = in.readString();
        accessToken = in.readString();
    }

    public static final Creator<Authentication> CREATOR = new Creator<Authentication>() {
        @Override
        public Authentication createFromParcel(final Parcel in) {
            return new Authentication(in);
        }

        @Override
        public Authentication[] newArray(final int size) {
            return new Authentication[size];
        }
    };

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(accessToken);
    }

    public static final class Builder {
        private String email;
        private String password;
        private String accessToken;

        public Builder() {

        }

        public Builder withEmail(@NonNull final String email) {
            this.email = email;
            return this;
        }

        public Builder withPassword(@NonNull final String password) {
            this.password = password;
            return this;
        }

        public Builder withAccessToken(@NonNull final String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Authentication build() {
            return new Authentication(this);
        }
    }
}
