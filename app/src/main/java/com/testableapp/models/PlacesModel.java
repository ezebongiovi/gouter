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

    public Observable<ApiResponse<List<Place>>> getPlaces(@NonNull final String criteria) {
        return getService().getPlaces(criteria);
    }

    private PlacesService getService() {
        return MainApplication.getRetrofit().create(PlacesService.class);
    }
}
