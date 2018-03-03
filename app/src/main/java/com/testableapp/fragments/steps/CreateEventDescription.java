package com.testableapp.fragments.steps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.stepstone.stepper.VerificationError;
import com.testableapp.R;
import com.testableapp.fragments.base.AbstractFragment;
import com.testableapp.models.EventsModel;
import com.testableapp.utils.PermissionUtils;
import com.testableapp.views.StepView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CreateEventDescription extends AbstractFragment implements StepView {

    private static final int PICK_IMAGE = 100;
    private static final int REQUEST_PERMISSIONS = 200;
    private ImageView mCoverView;
    private View mCoverEmptyView;
    private File mFile;
    private EditText mDescriptionView;

    @Override
    protected int getResourceId() {
        return R.layout.fragment_step_create_event_description;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final List<String> requiredPermissions = PermissionUtils.checkForPermissions(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (requiredPermissions.isEmpty()) {
                    selectFromGallery();
                } else {
                    requestPermissions(requiredPermissions.toArray(new String[requiredPermissions.size()]),
                            REQUEST_PERMISSIONS);
                }
            }
        };

        mDescriptionView = view.findViewById(R.id.descriptionField);
        mCoverEmptyView = view.findViewById(R.id.coverEmpty);
        mCoverView = view.findViewById(R.id.cover);
        mCoverView.setOnClickListener(listener);
        mCoverEmptyView.setOnClickListener(listener);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        final String message;

        if (mFile == null) {
            message = getString(R.string.error_verification_cover_empty);
        } else if (mDescriptionView.getText().toString().isEmpty()) {
            message = getString(R.string.error_verification_description_error);
        } else {
            message = null;
        }

        final boolean valid = mFile != null && !mDescriptionView.getText().toString().isEmpty();

        if (valid) {
            EventsModel.Repository.eventBuilder.setCoverFile(mFile)
                    .setDescription(mDescriptionView.getText().toString());
        }

        return valid ? null : new VerificationError(message);
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull final VerificationError error) {
        onError(error.getErrorMessage());
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (PICK_IMAGE == requestCode && resultCode == RESULT_OK) {
            if (data == null || data.getData() == null) {
                onError(getString(R.string.error_cover_message));
            } else {
                try {
                    mFile = new File(getFile(data.getData()));
                    displayImage(BitmapFactory.decodeStream(getContext()
                            .getContentResolver().openInputStream(data.getData())));
                } catch (final FileNotFoundException e) {
                    onError(getString(R.string.error_cover_message));
                }
            }
        }
    }

    public String getFile(final Uri contentUri) {
        Cursor cursor = null;
        try {
            final String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getContext().getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
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

    private void displayImage(final Bitmap bitmap) {
        if (bitmap == null) {
            mCoverEmptyView.setVisibility(View.VISIBLE);
            mCoverView.setVisibility(View.GONE);
        } else {
            mCoverView.setImageBitmap(bitmap);
            mCoverEmptyView.setVisibility(View.GONE);
            mCoverView.setVisibility(View.VISIBLE);
        }
    }

    void selectFromGallery() {
        final Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.extern_action_gallery_title)), PICK_IMAGE);
    }
}
