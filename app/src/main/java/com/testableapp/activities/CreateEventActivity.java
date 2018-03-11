package com.testableapp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.testableapp.R;
import com.testableapp.dto.FlowAction;
import com.testableapp.dto.GEvent;
import com.testableapp.dto.Guest;
import com.testableapp.dto.User;
import com.testableapp.presenters.CreateEventPresenter;
import com.testableapp.utils.PermissionUtils;
import com.testableapp.views.CreateEventView;
import com.testableapp.widgets.ContactPicker;
import com.testableapp.widgets.PlacePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateEventActivity extends AbstractMvpActivity<CreateEventPresenter>
        implements CreateEventView {
    private static final int PICK_IMAGE = 100;
    private static final int REQUEST_PERMISSIONS = 200;
    private File mFile;
    private View mContinueButton;
    private final GEvent.Builder mEventBuilder = new GEvent.Builder();
    private boolean dateComplete;
    private boolean timeComplete;

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
        setTitle(getString(R.string.activity_title_create_event));
        final Resources res = getResources();

        final List<FlowAction> steps = new ArrayList<>();

        steps.add(new FlowAction("Fecha", res.getDrawable(R.mipmap.action_flow_date)));
        steps.add(new FlowAction("Hora", res.getDrawable(R.mipmap.action_flow_time)));
        steps.add(new FlowAction("Portada", res.getDrawable(R.mipmap.action_flow_picture)));
        steps.add(new FlowAction("Dirección", res.getDrawable(R.mipmap.action_flow_place)));
        steps.add(new FlowAction("Invitados", res.getDrawable(R.mipmap.action_flow_guests)));
        steps.add(new FlowAction("Descripción", res.getDrawable(R.mipmap.action_flow_description)));

        final GridView recyclerView = findViewById(R.id.gridView);
        recyclerView.setAdapter(new FlowsAdapter(getSupportFragmentManager(), steps));

        mContinueButton = findViewById(R.id.buttonCreateEvent);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mPresenter.createEvent(mEventBuilder.build());
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
        startActivity(new Intent(CongratsActivity.getIntent(CreateEventActivity.this, event)));
        finish();
    }

    final class FlowsAdapter extends BaseAdapter {

        final List<FlowAction> mData;
        private final FragmentManager mFragmentManager;

        private FlowsAdapter(@NonNull final FragmentManager fm, @NonNull final List<FlowAction> actions) {
            mFragmentManager = fm;
            mData = actions;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(final int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return mData.get(position).hashCode();
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.holder_action_flow, parent, false);
            }

            final FlowAction flowAction = mData.get(position);

            ((ImageView) convertView.findViewById(R.id.icon)).setImageDrawable(flowAction.drawable);
            ((TextView) convertView.findViewById(R.id.text)).setText(flowAction.title);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    handleAction(position);
                }
            });

            return convertView;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (PICK_IMAGE == requestCode && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                mFile = new File(getFile(data.getData()));
            }
        }

        validate();
    }


    private void handleAction(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CreateEventActivity.this);
        builder.setCancelable(true);

        switch (position) {
            case 0:
                final Calendar calendar = Calendar.getInstance();
                final DatePicker datePicker = new DatePicker(CreateEventActivity.this);
                datePicker.setMinDate(calendar.getTime().getTime());

                builder.setPositiveButton(getString(R.string.button_accept), new DialogInterface.OnClickListener() {
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
                final TimePicker timePicker = new TimePicker(CreateEventActivity.this);
                builder.setView(timePicker);
                builder.setPositiveButton(getString(R.string.button_accept), new DialogInterface.OnClickListener() {
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
                final List<String> requiredPermissions = PermissionUtils.checkForPermissions(CreateEventActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (requiredPermissions.isEmpty()) {
                    selectFromGallery();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(requiredPermissions.toArray(new String[requiredPermissions.size()]),
                                REQUEST_PERMISSIONS);
                    }
                }

                break;
            case 3:
                final PlacePicker placePicker = new PlacePicker(CreateEventActivity.this);
                builder.setPositiveButton(getString(R.string.button_accept), new DialogInterface.OnClickListener() {
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
                final ContactPicker contactPicker = new ContactPicker(CreateEventActivity.this);
                contactPicker.setSelectable(true);
                builder.setPositiveButton(getString(R.string.button_accept), new DialogInterface.OnClickListener() {
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
                final EditText editText = (EditText) LayoutInflater.from(CreateEventActivity.this)
                        .inflate(R.layout.view_description, null, false);
                builder.setPositiveButton(getString(R.string.button_accept), new DialogInterface.OnClickListener() {
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

    public String getFile(final Uri contentUri) {
        Cursor cursor = null;
        try {
            final String[] proj = {MediaStore.Images.Media.DATA};
            cursor = CreateEventActivity.this.getContentResolver().query(contentUri, proj,
                    null, null, null);
            final int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectFromGallery();
        }
    }


    void selectFromGallery() {
        final Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.extern_action_gallery_title)), PICK_IMAGE);
    }

    private void validate() {
        final GEvent event = mEventBuilder.build();

        if (dateComplete && timeComplete && mFile != null && event.guests != null
                && !event.guests.isEmpty() && event.address != null && event.description != null) {
            mEventBuilder.setCoverFile(mFile);
            mContinueButton.setEnabled(true);
        } else {
            mContinueButton.setEnabled(false);
        }
    }
}
