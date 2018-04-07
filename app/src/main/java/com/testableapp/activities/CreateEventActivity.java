package com.testableapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.testableapp.R;
import com.testableapp.dto.GEvent;
import com.testableapp.presenters.CreateEventPresenter;
import com.testableapp.utils.PermissionUtils;
import com.testableapp.views.CreateEventView;
import com.testableapp.widgets.EventBuilderView;

import java.io.File;
import java.util.List;

public class CreateEventActivity extends AbstractMvpActivity<CreateEventPresenter>
        implements CreateEventView, EventBuilderView.EventBuilderListener {
    private static final int PICK_IMAGE = 100;
    private static final int REQUEST_PERMISSIONS = 200;
    private View mContinueButton;
    EventBuilderView mEventBuilderView;

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

        mEventBuilderView = findViewById(R.id.eventBuilderView);
        mEventBuilderView.init(this);

        mContinueButton = findViewById(R.id.buttonCreateEvent);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mPresenter.createEvent(mEventBuilderView.getEvent());
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


    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (PICK_IMAGE == requestCode && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                mEventBuilderView.setCover(new File(getFile(data.getData())));
            }
        }
    }

    private String getFile(final Uri contentUri) {
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

    @Override
    public void selectFromGallery() {
        final List<String> requiredPermissions = PermissionUtils.checkForPermissions(CreateEventActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (requiredPermissions.isEmpty()) {
            final Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent,
                    getString(R.string.extern_action_gallery_title)), PICK_IMAGE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(requiredPermissions.toArray(new String[requiredPermissions.size()]),
                        REQUEST_PERMISSIONS);
            }
        }
    }

    @Override
    public void onDataCompleted() {
        mContinueButton.setEnabled(true);
    }

    @Override
    public void onValidationError(@NonNull final String message) {
        mContinueButton.setEnabled(false);
        onError(message);
    }
}
