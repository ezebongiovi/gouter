package com.testableapp.dto;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Authentication implements Serializable {
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

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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
            this.password  = password;
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
