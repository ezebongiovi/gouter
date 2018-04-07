package com.testableapp.services;

import android.support.annotation.NonNull;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.CreateEvent;
import com.testableapp.dto.GEvent;
import com.testableapp.dto.GuestStatus;
import com.testableapp.dto.Search;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventsService {

    @Multipart
    @POST("/events")
    Observable<ApiResponse<GEvent>> createEvent(@Part MultipartBody.Part file,
                                                @Part("data") @NonNull CreateEvent createEvent);

    @GET("/events")
    Observable<ApiResponse<Search<GEvent>>> getEvents(@Query("offset") int offset,
                                                      @Query("limit") int limit);

    @GET("profile/me/events")
    Observable<ApiResponse<Search<GEvent>>> getMyEvents(@Query("offset") int offset,
                                                        @Query("limit") int limit);

    @PATCH("/events/{eventId}/guests/{guestId}")
    Observable<ApiResponse> switchAssistance(@Path("eventId") final String eventId,
                                             @Path("guestId") final String guestId,
                                             @Body final GuestStatus status);

    @Multipart
    @PATCH("/events/{eventId}")
    Observable<ApiResponse> editEvent(@Path("eventId") final String eventId, @Part MultipartBody.Part file,
                                      @Part("data") @NonNull CreateEvent createEvent);

    @PATCH("/events/{eventId}")
    Observable<ApiResponse> editEvent(@Path("eventId") final String eventId,
                                      @Body @NonNull CreateEvent createEvent);
}
