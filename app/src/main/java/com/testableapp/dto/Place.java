package com.testableapp.dto;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class Place {

    public final String formattedAddress;
    public final LatLng coords;

    public Place(@NonNull final String formattedAddress, @NonNull final LatLng coords) {
        this.formattedAddress = formattedAddress;
        this.coords = coords;
    }
}
