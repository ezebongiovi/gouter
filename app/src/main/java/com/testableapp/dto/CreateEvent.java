package com.testableapp.dto;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateEvent {
    private final String author;
    private final String description;
    private final Date date;
    private final List<String> guests = new ArrayList<>();
    private final String address;

    public CreateEvent(@NonNull final String author, @NonNull final GEvent gEvent) {
        this.author = author;
        this.description = gEvent.description;
        this.date = gEvent.date;
        this.address = gEvent.address;

        for (User user : gEvent.guests) {
            this.guests.add(user._id);
        }
    }

}
