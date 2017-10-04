package com.testableapp.models;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.testableapp.MainApplication;
import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.Place;
import com.testableapp.services.PlacesService;

import java.util.List;

import io.reactivex.Observable;

public class PlacesModel {

    private static PlacesModel INSTANCE;

    public static PlacesModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlacesModel();
        }

        return INSTANCE;
    }

    public Observable<ApiResponse<List<Place>>> getMockedPlaces(@NonNull final String criteria) {
        return Observable.range(0, 5).map(integer ->
                new Place("Genova 202" + integer.intValue(),
                        new LatLng(-38.0635712197085, -57.55051396970849)))
                .filter(place -> place.formattedAddress.contains(criteria))
                .toList().map(places -> new ApiResponse.Builder<List<Place>>()
                        .withData(places).withStatus("OK").build()).toObservable();
    }

    public Observable<ApiResponse<List<Place>>> getPlaces(@NonNull final String criteria) {
        return getService().getPlaces(criteria);
    }

    private PlacesService getService() {
        return MainApplication.getRetrofit().create(PlacesService.class);
    }
}
