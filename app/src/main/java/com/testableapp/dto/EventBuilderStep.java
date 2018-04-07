package com.testableapp.dto;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

public class EventBuilderStep {

    public final String title;
    public final Drawable drawable;

    public EventBuilderStep(@NonNull final String title,
                            @NonNull final Drawable drawable) {
        this.title = title;
        this.drawable = drawable;
    }
}
