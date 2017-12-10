package com.testableapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.testableapp.R;
import com.testableapp.activities.LoginActivity;
import com.testableapp.fragments.base.AbstractFragment;
import com.testableapp.manager.AuthenticationManager;

public class ProfileFragment extends AbstractFragment {

    @Override
    protected int getResourceId() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AuthenticationManager.getInstance().logOut(getContext());
                startActivity(new Intent(getContext(), LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().finish();
            }
        });
    }

    @Override
    public void showProgressLayout() {

    }

    @Override
    public void showRegularLayout() {

    }
}
