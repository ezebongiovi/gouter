package com.testableapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.testableapp.R;
import com.testableapp.dto.User;
import com.testableapp.fragments.ProfileFragment;
import com.testableapp.manager.AuthenticationManager;
import com.testableapp.presenters.ProfilePresenter;
import com.testableapp.views.ProfileView;

public class ProfileActivity extends AbstractMvpActivity<ProfilePresenter> implements ProfileView {

    private static final String EXTRA_USER = "extra-user";

    public static Intent getIntent(@NonNull final Context context, @NonNull final User user) {
        final Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(EXTRA_USER, user);

        return intent;
    }

    public ProfileActivity() {
        super(FLAG_BACK_ARROW);
    }

    @Override
    protected boolean shouldAuthenticate() {
        return true;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                 @NonNull final ProfilePresenter presenter) {
        if (getIntent().getExtras() == null || !getIntent().getExtras().containsKey(EXTRA_USER)) {
            throw new AssertionError("Use ProfileActivity.getIntent()");
        }

        //noinspection ConstantConditions
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                ProfileFragment.getInstance(getIntent().getExtras().getParcelable(EXTRA_USER))).commit();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_profile;
    }

    @NonNull
    @Override
    protected ProfilePresenter createPresenter() {
        return new ProfilePresenter();
    }
}
