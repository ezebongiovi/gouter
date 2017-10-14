package com.testableapp.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class Place implements Parcelable {

    public final String formattedAddress;
    public final LatLng coords;

    public Place(@NonNull final String formattedAddress, @NonNull final LatLng coords) {
        this.formattedAddress = formattedAddress;
        this.coords = coords;
    }

    protected Place(final Parcel in) {
        formattedAddress = in.readString();
        coords = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(final Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(final int size) {
            return new Place[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(formattedAddress);
        dest.writeParcelable(coords, flags);
    }
}
