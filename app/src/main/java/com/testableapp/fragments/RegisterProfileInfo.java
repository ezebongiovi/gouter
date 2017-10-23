package com.testableapp.fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.stepstone.stepper.VerificationError;
import com.testableapp.R;
import com.testableapp.fragments.base.AbstractFragment;
import com.testableapp.views.StepView;

public class RegisterProfileInfo extends AbstractFragment implements StepView {
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

    @Override
    protected int getResourceId() {
        return R.layout.fragment_register_profile;
    }
}
