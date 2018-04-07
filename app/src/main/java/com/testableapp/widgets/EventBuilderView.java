package com.testableapp.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TimePicker;

import com.testableapp.R;
import com.testableapp.adapters.EventBuilderStepsAdapter;
import com.testableapp.dto.EventBuilderStep;
import com.testableapp.dto.GEvent;
import com.testableapp.dto.Guest;
import com.testableapp.dto.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventBuilderView extends GridView {

    private EventBuilderListener mListener;
    private ValueChangeListener mValueListener;

    public interface EventBuilderListener {
        void selectFromGallery();

        void onDataCompleted();

        void onValidationError(@NonNull final String message);
    }

    public interface ValueChangeListener {
        void onValueChange(@NonNull GEvent event);
    }

    private final GEvent.Builder mEventBuilder = new GEvent.Builder();
    private boolean dateComplete;
    private boolean timeComplete;
    private File mFile;

    public EventBuilderView(final Context context) {
        this(context, null);
    }

    public EventBuilderView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventBuilderView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(@NonNull final EventBuilderListener listener,
                     @NonNull final ValueChangeListener valueChangeListener) {
        mListener = listener;
        mValueListener = valueChangeListener;

        final Resources res = getContext().getResources();
        final List<EventBuilderStep> steps = new ArrayList<>();

        steps.add(new EventBuilderStep("Fecha", res.getDrawable(R.mipmap.action_flow_date)));
        steps.add(new EventBuilderStep("Hora", res.getDrawable(R.mipmap.action_flow_time)));
        steps.add(new EventBuilderStep("Portada", res.getDrawable(R.mipmap.action_flow_picture)));
        steps.add(new EventBuilderStep("Dirección", res.getDrawable(R.mipmap.action_flow_place)));
        steps.add(new EventBuilderStep("Invitados", res.getDrawable(R.mipmap.action_flow_guests)));
        steps.add(new EventBuilderStep("Descripción", res.getDrawable(R.mipmap.action_flow_description)));

        setAdapter(new EventBuilderStepsAdapter(new EventBuilderStepsAdapter.OnClickListener() {
            @Override
            public void onClick(final int position) {
                handleAction(position);
            }
        }, steps));
    }

    public void init(@NonNull final EventBuilderListener listener) {
        init(listener, null);
    }

    private void handleAction(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                dialog.dismiss();
            }
        });

        switch (position) {
            case 0:
                final Calendar calendar = Calendar.getInstance();
                final DatePicker datePicker = new DatePicker(getContext());
                datePicker.setMinDate(calendar.getTime().getTime());

                builder.setPositiveButton(getContext().getString(R.string.button_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        final int day = datePicker.getDayOfMonth();
                        final int month = datePicker.getMonth();
                        final int year = datePicker.getYear();

                        final Calendar calendar = Calendar.getInstance();
                        final GEvent gEvent = mEventBuilder.build();

                        if (gEvent.date != null) {
                            calendar.setTime(gEvent.date);
                        }

                        calendar.set(year, month, day);

                        mEventBuilder.setDate(calendar.getTime());
                        dateComplete = true;
                        validate();
                    }
                });

                builder.setView(datePicker);
                builder.create().show();
                break;
            case 1:
                final TimePicker timePicker = new TimePicker(getContext());
                builder.setView(timePicker);
                builder.setPositiveButton(getContext().getString(R.string.button_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        final GEvent gEvent = mEventBuilder.build();
                        final Calendar c = Calendar.getInstance();

                        if (gEvent.date != null) {
                            c.setTime(gEvent.date);
                        }

                        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                                timePicker.getCurrentHour(), timePicker.getCurrentMinute());

                        mEventBuilder.setDate(c.getTime());
                        timeComplete = true;
                        validate();
                    }
                });

                builder.create().show();
                break;
            case 2:
                mListener.selectFromGallery();
                break;
            case 3:
                final PlacePicker placePicker = new PlacePicker(getContext());
                builder.setPositiveButton(getContext().getString(R.string.button_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        mEventBuilder.setAddress(placePicker.getSelectedPlace());
                        validate();
                    }
                });

                builder.setView(placePicker);
                builder.create().show();
                break;
            case 4:
                final ContactPicker contactPicker = new ContactPicker(getContext());
                contactPicker.setSelectable(true);
                builder.setPositiveButton(getContext().getString(R.string.button_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        final List<Guest> guestList = new ArrayList<>();
                        for (final User user : contactPicker.getSelectedContacts()) {
                            guestList.add(new Guest(user, Guest.STATUS_PENDING));
                        }

                        mEventBuilder.setGuests(guestList);
                        validate();
                    }
                });
                builder.setView(contactPicker);
                builder.create().show();
                break;
            case 5:
                final EditText editText = (EditText) LayoutInflater.from(getContext())
                        .inflate(R.layout.view_description, null, false);
                builder.setPositiveButton(getContext().getString(R.string.button_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        mEventBuilder.setDescription(editText.getText().toString());
                        validate();
                    }
                });
                builder.setView(editText);
                builder.create().show();
                break;
        }
    }

    public GEvent getEvent() {
        return mEventBuilder.build();
    }

    public void setCover(@NonNull final File file) {
        mFile = file;
        validate();
    }

    private void validate() {
        final GEvent event = mEventBuilder.build();

        if (dateComplete && event.date != null && event.date.compareTo(Calendar.getInstance().getTime()) <= 0) {
            mListener.onValidationError(getContext().getString(R.string.error_verification_date));
            return;
        }

        if (dateComplete && timeComplete && mFile != null && event.guests != null
                && !event.guests.isEmpty() && event.address != null && event.description != null) {

            mListener.onDataCompleted();
        }

        if (mFile != null) {
            mEventBuilder.setCoverFile(mFile);
        }

        if (mValueListener != null) {
            mValueListener.onValueChange(mEventBuilder.build());
        }
    }
}
