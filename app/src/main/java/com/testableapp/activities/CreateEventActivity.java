package com.testableapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.testableapp.R;
import com.testableapp.adapters.StepAdapter;
import com.testableapp.dto.GEvent;
import com.testableapp.fragments.steps.CreateEventAddress;
import com.testableapp.fragments.steps.CreateEventConfirm;
import com.testableapp.fragments.steps.CreateEventDate;
import com.testableapp.fragments.steps.CreateEventDescription;
import com.testableapp.fragments.steps.CreateEventGuests;
import com.testableapp.manager.AuthenticationManager;
import com.testableapp.models.EventsModel;
import com.testableapp.presenters.CreateEventPresenter;
import com.testableapp.views.CreateEventView;
import com.testableapp.views.StepView;

import java.util.ArrayList;
import java.util.List;

public class CreateEventActivity extends AbstractMvpActivity<CreateEventPresenter>
        implements CreateEventView {

    private StepperLayout mStepper;

    public CreateEventActivity() {
        super(FLAG_BACK_ARROW);
    }

    @Override
    protected boolean shouldAuthenticate() {
        return true;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                 @NonNull final CreateEventPresenter presenter) {
        mStepper = findViewById(R.id.stepperLayout);

        final List<StepView> steps = new ArrayList<>();
        steps.add(new CreateEventAddress());
        steps.add(new CreateEventDescription());
        steps.add(new CreateEventDate());
        steps.add(new CreateEventGuests());
        steps.add(new CreateEventConfirm());

        mStepper.setAdapter(new StepAdapter(getSupportFragmentManager(),
                CreateEventActivity.this, steps));

        mStepper.setListener(new StepperLayout.StepperListener() {
            @Override
            public void onCompleted(final View completeButton) {
                final GEvent gEvent = EventsModel.Repository.eventBuilder
                        .setAuthor(AuthenticationManager.getInstance()
                                .getUser(CreateEventActivity.this)).build();

                presenter.createEvent(gEvent);
            }

            @Override
            public void onError(final VerificationError verificationError) {
                // Nothing to do
            }

            @Override
            public void onStepSelected(int newStepPosition) {
                // Nothing to do
            }

            @Override
            public void onReturn() {

            }
        });
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_create_event;
    }

    @NonNull
    @Override
    protected CreateEventPresenter createPresenter() {
        return new CreateEventPresenter();
    }

    @Override
    public void onEventCreated(final GEvent event) {
        // TODO: Handle response
    }
}
