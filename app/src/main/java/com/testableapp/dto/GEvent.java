package com.testableapp.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.zxing.common.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GEvent implements Parcelable {

    public final User author;
    public final Date date;
    public final String description;
    public final Place address;
    public final Cover cover;
    public final List<Guest> guests;
    public transient final File coverFile;
    public String _id;

    private GEvent(final Builder builder) {
        this._id = builder._id;
        this.date = builder.date;
        this.description = builder.description;
        this.address = builder.address;
        this.guests = builder.guests;
        this.author = builder.author;
        this.cover = builder.cover;
        this.coverFile = builder.coverFile;
    }

    protected GEvent(final Parcel in) {
        author = in.readParcelable(User.class.getClassLoader());
        description = in.readString();
        address = in.readParcelable(Place.class.getClassLoader());
        date = new Date(in.readLong());
        guests = in.createTypedArrayList(Guest.CREATOR);
        this.cover = in.readParcelable(Cover.class.getClassLoader());
        this.coverFile = (File) in.readSerializable();
        this._id = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(author, flags);
        dest.writeString(description);
        dest.writeParcelable(address, flags);
        dest.writeLong(date.getTime());
        dest.writeTypedList(guests);
        dest.writeParcelable(cover, flags);
        dest.writeSerializable(coverFile);
        dest.writeString(_id);
    }

    public static final class Builder {

        private Date date;
        private String description;
        private Place address;
        private List<Guest> guests = new ArrayList<>();
        private User author;
        private Cover cover;
        private File coverFile;
        private String _id;

        public Builder() {

        }

        public Builder(@NonNull final GEvent event) {
            setDate(event.date);
            setCoverFile(event.coverFile);
            setCover(event.cover);
            setDescription(event.description);
            setId(event._id);
            setAddress(event.address);
            setGuests(event.guests);
            setAuthor(event.author);
        }

        public Builder setDescription(@NonNull final String description) {
            this.description = description;
            return this;
        }

        public Builder setAuthor(@NonNull final User author) {
            this.author = author;
            return this;
        }

        public Builder setAddress(@NonNull final Place address) {
            this.address = address;
            return this;
        }

        public Builder setGuests(@NonNull final List<Guest> guests) {
            this.guests = guests;
            return this;
        }

        public Builder setCover(@NonNull final Cover cover) {
            this.cover = cover;
            return this;
        }

        public Builder setCoverFile(@NonNull final File file) {
            this.coverFile = file;
            return this;
        }

        public Builder setDate(@NonNull final Date date) {
            this.date = date;
            return this;
        }

        public Builder setId(@NonNull final String id) {
            this._id = id;
            return this;
        }

        public Builder copyData(@NonNull final GEvent event) {
            if (event.date != null) {
                setDate(event.date);
            }

            if (event.coverFile != null) {
                setCoverFile(event.coverFile);
            }

            if (event.cover != null) {
                setCover(event.cover);
            }

            if (event.author != null) {
                setAuthor(event.author);
            }

            if (event.address != null) {
                setAddress(event.address);
            }

            if (event.guests != null && !event.guests.isEmpty()) {
                setGuests(event.guests);
            }

            if (event._id != null) {
                setId(event._id);
            }

            if (event.description != null) {
                setDescription(event.description);
            }

            return this;
        }

        public GEvent build() {
            return new GEvent(this);
        }
    }
}
