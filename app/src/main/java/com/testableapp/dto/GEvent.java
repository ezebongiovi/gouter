package com.testableapp.dto;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class GEvent extends BaseObservable implements Parcelable {

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

    protected GEvent(final Parcel in) {
        author = in.readParcelable(User.class.getClassLoader());
        description = in.readString();
        address = in.readString();
        date = new Date(in.readLong());
        guests = in.createTypedArrayList(User.CREATOR);
    }

    public static final Creator<GEvent> CREATOR = new Creator<GEvent>() {
        @Override
        public GEvent createFromParcel(final Parcel in) {
            return new GEvent(in);
        }

        @Override
        public GEvent[] newArray(final int size) {
            return new GEvent[size];
        }
    };

    @Bindable
    public Date getDate() {
        return date;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    @Bindable
    public List<User> getGuests() {
        return guests;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(author, flags);
        dest.writeString(description);
        dest.writeString(address);
        dest.writeLong(date.getTime());
        dest.writeTypedList(guests);
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
