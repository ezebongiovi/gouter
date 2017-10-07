package com.testableapp.services;

import android.support.annotation.NonNull;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.CreateEvent;
import com.testableapp.dto.GEvent;
import com.testableapp.dto.Search;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EventsService {

    @POST("/events")
    Observable<ApiResponse<GEvent>> createEvent(@Body @NonNull CreateEvent createEvent);

    @GET("/events")
    Observable<ApiResponse<Search<GEvent>>> getEvents(@Query("offset") int offset,
                                                      @Query("limit") int limit);

    @GET("profile/me/events")
    Observable<ApiResponse<Search<GEvent>>> getMyEvents(@Query("offset") int offset,
                                                        @Query("limit") int limit);
}
