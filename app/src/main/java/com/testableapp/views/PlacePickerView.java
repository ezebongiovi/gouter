package com.testableapp.views;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.testableapp.dto.Place;

import java.util.List;

public interface PlacePickerView extends OnMapReadyCallback {
    void onSearchResult(@NonNull List<Place> data);

    void onError(@NonNull String message);

    void onNetworkError();

    void onGenericError();

    void onPlaceSelected(@NonNull Place place);
}
