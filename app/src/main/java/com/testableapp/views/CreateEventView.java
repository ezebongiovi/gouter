package com.testableapp.views;

import com.testableapp.dto.GEvent;

public interface CreateEventView extends AbstractView {
    void onEventCreated(final GEvent event);
}
