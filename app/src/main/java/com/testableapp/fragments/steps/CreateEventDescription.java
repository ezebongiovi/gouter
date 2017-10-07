package com.testableapp.fragments.steps;

import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.stepstone.stepper.VerificationError;
import com.testableapp.R;
import com.testableapp.fragments.base.AbstractFragment;
import com.testableapp.utils.PermissionUtils;
import com.testableapp.views.StepView;

import java.io.FileNotFoundException;

import static android.app.Activity.RESULT_OK;

public class CreateEventDescription extends AbstractFragment implements StepView {

    private static final int PICK_IMAGE = 100;
    private ImageView mCoverView;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_step_create_event_description, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCoverView = view.findViewById(R.id.cover);
        mCoverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (PermissionUtils.checkForPermissions(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE).isEmpty()) {
                    selectFromGallery();
                }
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
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull final VerificationError error) {

    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (PICK_IMAGE == requestCode && resultCode == RESULT_OK) {
            if (data == null || data.getData() == null) {
                onError(getString(R.string.error_cover_message));
            } else {
                try {
                    mCoverView.setImageBitmap(BitmapFactory.decodeStream(getContext()
                            .getContentResolver().openInputStream(data.getData())));
                } catch (final FileNotFoundException e) {
                    onError(getString(R.string.error_cover_message));
                }
            }
        }
    }

    private void selectFromGallery() {
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.extern_action_gallery_title)), PICK_IMAGE);
    }
}
