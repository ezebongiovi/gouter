package com.testableapp.views;

import android.support.annotation.NonNull;

import com.testableapp.dto.GEvent;

import java.util.List;

public interface EventsView extends AbstractView {
    void showEvents(@NonNull List<GEvent> data);

    void showListProgress();

    void hideListProgress();

    void showEmptyState();

    void addEvents(@NonNull List<GEvent> results);
}
