package com.testableapp.dto;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class GEvent {

    private final User author;
    private final Date date;
    private final String description;
    private final String address;
    private final List<User> guests;

    private GEvent(final Builder builder) {
        this.date = builder.date;
        this.description = builder.description;
        this.address = builder.address;
        this.guests = builder.guests;
        this.author = builder.author;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public List<User> getGuests() {
        return guests;
    }

    public static final class Builder {

        private Date date;
        private String description;
        private String address;
        private List<User> guests;
        private User author;

        public Builder() {

        }

        public Builder setDescription(@NonNull final String description) {
            this.description = description;
            return this;
        }

        public Builder setAuthor(@NonNull final User author) {
            this.author = author;
            return this;
        }

        public Builder setAddress(@NonNull final String address) {
            this.address = address;
            return this;
        }

        public Builder setGuests(@NonNull final List<User> guests) {
            this.guests = guests;
            return this;
        }

        public Builder setDate(@NonNull final Date date) {
            this.date = date;
            return this;
        }

        public GEvent build() {
            return new GEvent(this);
        }
    }
}
