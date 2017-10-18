package com.testableapp.fragments.steps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.stepstone.stepper.VerificationError;
import com.testableapp.R;
import com.testableapp.fragments.base.AbstractFragment;
import com.testableapp.models.EventsModel;
import com.testableapp.views.StepView;

import java.util.Calendar;

public class CreateEventDate extends AbstractFragment implements StepView {

    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    int mHour;
    int mMinute;

    @Override
    protected int getResourceId() {
        return R.layout.fragment_step_create_event_date;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatePicker = view.findViewById(R.id.datePicker);
        mTimePicker = view.findViewById(R.id.timePicker);

        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(final TimePicker view, final int hourOfDay, final int minute) {
                mHour = hourOfDay;
                mMinute = minute;
            }
        });
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(),
                mHour, mMinute);

        EventsModel.Repository.eventBuilder.setDate(calendar.getTime());

        if (Calendar.getInstance().getTime().getTime() < calendar.getTime().getTime()) {
            return null;
        }

        return new VerificationError(getString(R.string.error_verification_date));
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull final VerificationError error) {
        onError(error.getErrorMessage());
    }
}
