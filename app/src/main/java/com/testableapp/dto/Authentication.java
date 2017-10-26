package com.testableapp.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Authentication implements Parcelable {

    public static final String PROVIDER_FACEBOOK = "Facebook";
    public final String accessToken;
    public final String providerName;

    @StringDef(PROVIDER_FACEBOOK)
    @Retention(RetentionPolicy.SOURCE)
    @interface Provider {}

    private Authentication(final Builder builder) {
        this.accessToken = builder.accessToken;
        this.providerName = builder.providerName;
    }

    protected Authentication(final Parcel in) {
        this.accessToken = in.readString();
        this.providerName = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(accessToken);
        dest.writeString(providerName);
    }

    public static final class Builder {
        private String accessToken;
        private String providerName;

        public Builder() {

        }

        public Builder withAccessToken(@NonNull final String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder withProviderName(@NonNull @Provider final String providerName) {
            this.providerName = providerName;
            return this;
        }

        public Authentication build() {
            return new Authentication(this);
        }
    }
}
