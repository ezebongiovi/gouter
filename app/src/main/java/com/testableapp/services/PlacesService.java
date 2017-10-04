package com.testableapp.services;

import android.support.annotation.NonNull;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.Place;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesService {
    @GET("sarasa")
    Observable<ApiResponse<List<Place>>> getPlaces(@NonNull @Query("query") String query);
}
