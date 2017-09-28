package com.testableapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ViewFlipper;

import com.testableapp.R;
import com.testableapp.dto.GEvent;
import com.testableapp.manager.AuthenticationManager;
import com.testableapp.presenters.CreateEventPresenter;
import com.testableapp.views.CreateEventView;
import com.testableapp.widgets.ContactPicker;

import java.util.Calendar;

public class CreateEventActivity extends AbstractMvpActivity<CreateEventPresenter>
        implements CreateEventView {

    private int mStep = 0;

    @Override
    protected boolean shouldAuthenticate() {
        return true;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                 @NonNull final CreateEventPresenter presenter) {
        final ViewFlipper viewFlipper = findViewById(R.id.viewFlipper);
        viewFlipper.setInAnimation(this, R.anim.slide_down_in);
        viewFlipper.setOutAnimation(this, R.anim.slide_down_out);

        final DatePicker datePicker = findViewById(R.id.datePicker);
        final TimePicker timePicker = findViewById(R.id.timePicker);
        final ContactPicker contactPicker = findViewById(R.id.contactPicker);
        final EditText addressView = findViewById(R.id.addressView);
        final EditText descriptionView = findViewById(R.id.descriptionView);

        final Calendar calendar = Calendar.getInstance();
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(final DatePicker view, final int year,
                                              final int monthOfYear, final int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                    }
                });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
            }
        });

        findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                // TODO: Handle event creation steps

                if (mStep < viewFlipper.getChildCount()) {
                    viewFlipper.showNext();
                    mStep++;
                } else {
                    presenter.createEvent(AuthenticationManager.getInstance().getUser(CreateEventActivity.this)
                                    .getId(), addressView.getText().toString(), calendar.getTime(),
                            descriptionView.getText().toString(), contactPicker.getSelectedContacts());
                }
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
