package com.testableapp.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.testableapp.R;
import com.testableapp.databinding.ProfileActivityBinding;
import com.testableapp.dto.User;
import com.testableapp.presenters.ProfilePresenter;

public class ProfileActivity extends AbstractActivity<ProfilePresenter> {

    private static final String EXTRA_USER = "extra_user";

    public static Intent getIntent(@NonNull final Context context, @NonNull final User user) {
        final Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(EXTRA_USER, user);

        return intent;
    }

    @Override
    protected boolean shouldAuthenticate() {
        return true;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                 final @NonNull ProfilePresenter presenter) {

        if (getIntent().getExtras().containsKey(EXTRA_USER)) {
            final ProfileActivityBinding binding = DataBindingUtil
                    .setContentView(this, R.layout.profile_activity);

            final User user = (User) getIntent().getExtras().get(EXTRA_USER);
            binding.setUser(user);

        } else {
            finish();
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.profile_activity;
    }

    @NonNull
    @Override
    protected ProfilePresenter createPresenter() {
        return new ProfilePresenter();
    }
}
