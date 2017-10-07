package com.testableapp.models;

import android.support.annotation.NonNull;

import com.testableapp.MainApplication;
import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.CreateEvent;
import com.testableapp.dto.GEvent;
import com.testableapp.dto.Search;
import com.testableapp.services.EventsService;

import io.reactivex.Observable;

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

    public Observable<ApiResponse<GEvent>> createEvent(@NonNull final CreateEvent createEvent) {
        return getService().createEvent(createEvent);
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
}
