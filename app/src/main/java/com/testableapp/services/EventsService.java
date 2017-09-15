package com.testableapp.services;

import android.support.annotation.NonNull;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.CreateEvent;
import com.testableapp.dto.GEvent;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EventsService {

    @POST("/events")
    Observable<ApiResponse<GEvent>> createEvent(@Body @NonNull CreateEvent createEvent);
}
