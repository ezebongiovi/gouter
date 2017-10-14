package com.testableapp.dto;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateEvent {
    @SerializedName("author")
    private final String author;
    @SerializedName("description")
    private final String description;
    @SerializedName("date")
    private final Date date;
    @SerializedName("guests")
    private final List<String> guests = new ArrayList<>();
    @SerializedName("address")
    private final Place address;

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
