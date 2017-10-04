package com.testableapp.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Country implements Parcelable {

    public final String name;

    public Country(@NonNull final String name) {
        this.name = name;
    }

    protected Country(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeString(name);
    }
}
