package com.testableapp.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testableapp.R;
import com.testableapp.activities.LoginActivity;
import com.testableapp.databinding.FragmentProfileBinding;
import com.testableapp.fragments.base.AbstractFragment;
import com.testableapp.manager.AuthenticationManager;

public class ProfileFragment extends AbstractFragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        final FragmentProfileBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_profile, container, false);

        binding.setUser(AuthenticationManager.getInstance().getUser(getContext()));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AuthenticationManager.getInstance().logOut(getContext());
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
    }
}
