package com.testableapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.testableapp.R;
import com.testableapp.activities.ProfileActivity;
import com.testableapp.dto.User;
import com.testableapp.fragments.base.AbstractFragment;
import com.testableapp.widgets.ContactPicker;

public class ContactsFragment extends AbstractFragment implements ContactPicker.ContactListener {

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((ContactPicker) view.findViewById(R.id.contactPicker)).setListener(this);
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_contacts;
    }

    @Override
    public void onSelectedContact(@NonNull final User contact) {
        startActivity(ProfileActivity.getIntent(getContext(), contact));
    }
}
