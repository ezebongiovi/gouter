package com.testableapp.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Cover implements Parcelable {
    public final String url;

    public Cover(@NonNull final String url) {
        this.url = url;
    }

    protected Cover(final Parcel in) {
        url = in.readString();
    }

    public static final Creator<Cover> CREATOR = new Creator<Cover>() {
        @Override
        public Cover createFromParcel(final Parcel in) {
            return new Cover(in);
        }

        @Override
        public Cover[] newArray(final int size) {
            return new Cover[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
    }
}
