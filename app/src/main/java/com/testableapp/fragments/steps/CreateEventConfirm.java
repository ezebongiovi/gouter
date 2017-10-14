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
import com.testableapp.dto.GEvent;
import com.testableapp.views.StepView;

public class CreateEventConfirm extends Fragment implements StepView {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step_create_event_confirm,
                container, false);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull final VerificationError error) {

    }
}
