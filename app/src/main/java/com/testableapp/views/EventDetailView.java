package com.testableapp.views;

import android.support.annotation.NonNull;

import com.testableapp.dto.GEvent;

/**
 * Created by epasquale on 28/11/17.
 */

public interface EventDetailView extends AbstractView {
    void onInvitationAccepted();

    void onInvitationRejected();

    void onEditionSuccess(@NonNull GEvent event);
}
