package com.testableapp.fragments.steps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stepstone.stepper.VerificationError;
import com.testableapp.R;
import com.testableapp.fragments.base.AbstractFragment;
import com.testableapp.models.EventsModel;
import com.testableapp.views.StepView;
import com.testableapp.widgets.ContactPicker;

public class CreateEventGuests extends AbstractFragment implements StepView {

    private ContactPicker mContactPicker;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_step_create_event_guests, container, false);
    }

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

        EventsModel.Repository.eventBuilder.setGuests(mContactPicker.getSelectedContacts());

        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull final VerificationError error) {
        onError(error.getErrorMessage());
    }
}
