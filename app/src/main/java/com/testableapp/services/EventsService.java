package com.testableapp.services;

import android.support.annotation.NonNull;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.CreateEvent;
import com.testableapp.dto.GEvent;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EventsService {

    @POST("/events")
    Observable<ApiResponse<GEvent>> createEvent(@Body @NonNull CreateEvent createEvent);

    @GET("/events")
    Observable<ApiResponse<List<GEvent>>> getEvents();
}
