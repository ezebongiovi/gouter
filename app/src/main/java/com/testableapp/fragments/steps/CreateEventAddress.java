package com.testableapp.fragments.steps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.stepstone.stepper.VerificationError;
import com.testableapp.R;
import com.testableapp.fragments.base.AbstractFragment;
import com.testableapp.models.EventsModel;
import com.testableapp.views.StepView;
import com.testableapp.widgets.PlacePicker;

public class CreateEventAddress extends AbstractFragment implements StepView {

    private PlacePicker mPlacePicker;

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPlacePicker = view.findViewById(R.id.placePicker);
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_step_create_event_address;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        EventsModel.Repository.eventBuilder.setAddress(mPlacePicker.getSelectedPlace());

        return mPlacePicker.getSelectedPlace() == null
                ? new VerificationError(getString(R.string
                .error_verification_address_empty)) : null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull final VerificationError error) {
        onError(error.getErrorMessage());
    }
}
