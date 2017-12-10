package com.testableapp.dto;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class CreateEvent {
    @SerializedName("description")
    private final String description;
    @SerializedName("date")
    private final Date date;
    @SerializedName("guests")
    private final List<Guest> guests;
    @SerializedName("address")
    private final Place address;

    public CreateEvent(@NonNull final GEvent gEvent) {
        this.description = gEvent.description;
        this.date = gEvent.date;
        this.address = gEvent.address;
        this.guests = gEvent.guests;
    }

}
