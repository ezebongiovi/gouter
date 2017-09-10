package com.testableapp.dto;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Authentication implements Serializable {
    private final String email;
    private final String password;
    private final String token;

    public Authentication(@NonNull final String email, @NonNull final String password) {
        this.email = email;
        this.password = password;
        this.token = null;
    }

    private Authentication(final Builder builder) {
        this.token = builder.token;
        this.email = builder.email;
        this.password = builder.password;
    }

    public String getToken() {
        return token;
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
        private String token;

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

        public Builder withToken(@NonNull final String token) {
            this.token = token;
            return this;
        }

        public Authentication build() {
            return new Authentication(this);
        }
    }
}
