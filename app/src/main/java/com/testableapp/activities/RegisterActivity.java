package com.testableapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.testableapp.R;
import com.testableapp.adapters.StepAdapter;
import com.testableapp.dto.User;
import com.testableapp.fragments.RegisterCellphone;
import com.testableapp.fragments.RegisterProfileInfo;
import com.testableapp.manager.AuthenticationManager;
import com.testableapp.presenters.RegisterPresenter;
import com.testableapp.views.RegisterView;
import com.testableapp.views.StepView;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AbstractMvpActivity<RegisterPresenter>
        implements RegisterView {

    private StepperLayout mStepper;

    public RegisterActivity() {
        super(FLAG_ROOT_VIEW);
    }

    @Override
    protected boolean shouldAuthenticate() {
        return false;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                 @NonNull final RegisterPresenter presenter) {
        mStepper = findViewById(R.id.stepperLayout);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final List<StepView> steps = new ArrayList<>();
        steps.add(new RegisterCellphone());
        steps.add(new RegisterProfileInfo());

        mStepper.setAdapter(new StepAdapter(getSupportFragmentManager(),
                RegisterActivity.this, steps));

        mStepper.setListener(new StepperLayout.StepperListener() {
            @Override
            public void onCompleted(final View completeButton) {
                // TODO: Register
            }

            @Override
            public void onError(final VerificationError verificationError) {
                // Nothing to do
            }

            @Override
            public void onStepSelected(final int newStepPosition) {
                // Nothing to do
            }

            @Override
            public void onReturn() {

            }
        });
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_register;
    }

    @NonNull
    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public void onInvalidData() {
        Snackbar.make(findViewById(R.id.rootView), "Los datos son inválidos", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onRegister(@NonNull final User user) {
        AuthenticationManager.getInstance().onLogin(this, user);
        startActivity(new Intent(this, NavigationActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
