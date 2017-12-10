package com.testableapp.fragments.steps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.stepstone.stepper.VerificationError;
import com.testableapp.R;
import com.testableapp.dto.Guest;
import com.testableapp.dto.User;
import com.testableapp.fragments.base.AbstractFragment;
import com.testableapp.models.EventsModel;
import com.testableapp.views.StepView;
import com.testableapp.widgets.ContactPicker;

import java.util.ArrayList;
import java.util.List;

public class CreateEventGuests extends AbstractFragment implements StepView {

    private ContactPicker mContactPicker;

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContactPicker = view.findViewById(R.id.contactPicker);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {

        if (mContactPicker.getSelectedContacts().isEmpty()) {
            return new VerificationError(getString(R.string.error_verification_guest_empty));
        }

        final List<Guest> guestList = new ArrayList<>();
        for (final User user : mContactPicker.getSelectedContacts()) {
            guestList.add(new Guest(user, Guest.STATUS_PENDING));
        }

        EventsModel.Repository.eventBuilder.setGuests(guestList);

        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull final VerificationError error) {
        onError(error.getErrorMessage());
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_step_create_event_guests;
    }
}
