package com.testableapp.models;

import android.support.annotation.NonNull;

import com.testableapp.MainApplication;
import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.CreateEvent;
import com.testableapp.dto.GEvent;
import com.testableapp.dto.Guest;
import com.testableapp.dto.GuestStatus;
import com.testableapp.dto.Search;
import com.testableapp.services.EventsService;

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

    public Observable<ApiResponse<GEvent>> createEvent(@NonNull final GEvent event) {
        final RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), event.coverFile);

        // MultipartBody.Part is used to send also the actual file name
        final MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", event.coverFile.getName(), requestFile);

        return getService().createEvent(body, new CreateEvent(event));
    }

    private EventsService getService() {
        return MainApplication.getRetrofit().create(EventsService.class);
    }

    public Observable<ApiResponse<Search<GEvent>>> getEvents(final int type, final int offset,
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

        return getService().switchAssistance(event._id, guest.user._id, new GuestStatus(status));
    }

    public Observable<ApiResponse> editEvent(@NonNull final GEvent data) {
        if (data.coverFile != null) {
            final RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), data.coverFile);

            // MultipartBody.Part is used to send also the actual file name
            final MultipartBody.Part body =
                    MultipartBody.Part.createFormData("picture", data.coverFile.getName(), requestFile);

            return getService().editEvent(data._id, body, new CreateEvent(data));
        } else {
            return getService().editEvent(data._id, new CreateEvent(data));
        }
    }
}
