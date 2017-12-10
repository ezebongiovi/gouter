package com.testableapp.models;

import android.support.annotation.NonNull;

import com.testableapp.MainApplication;
import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.CreateEvent;
import com.testableapp.dto.GEvent;
import com.testableapp.dto.Guest;
import com.testableapp.dto.GuestStatus;
import com.testableapp.dto.Search;
import com.testableapp.dto.User;
import com.testableapp.services.EventsService;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EventsModel {

    private static EventsModel INSTANCE;
    public static final int MY_EVENTS = 0;
    public static final int EVENTS = 1;

    private EventsModel() {

    }

    public static EventsModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventsModel();
        }

        return INSTANCE;
    }

    public Observable<ApiResponse<GEvent>> createEvent(@NonNull final File coverImage,
                                                       @NonNull final CreateEvent createEvent) {
        final RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), coverImage);

        // MultipartBody.Part is used to send also the actual file name
        final MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", coverImage.getName(), requestFile);

        return getService().createEvent(body, createEvent);
    }

    private EventsService getService() {
        return MainApplication.getRetrofit().create(EventsService.class);
    }

    public Observable<ApiResponse<Search<GEvent>>> getEvents(final int type,  final int offset,
                                                             final int limit) {
        if (type == MY_EVENTS) {
            return getService().getMyEvents(offset, limit);
        }

        return getService().getEvents(offset, limit);
    }

    public Observable<ApiResponse> switchAssistance(final GEvent event, final Guest guest) {
        final String status;

        if (guest.status.equals(Guest.STATUS_ACCEPTED)) {
            status = Guest.STATUS_REJECTED;
        } else {
            status = Guest.STATUS_ACCEPTED;
        }

        return getService().switchAssistance(event._id, guest.user._id , new GuestStatus(status));
    }

    public static final class Repository {
        public static final GEvent.Builder eventBuilder = new GEvent.Builder();
    }
}
