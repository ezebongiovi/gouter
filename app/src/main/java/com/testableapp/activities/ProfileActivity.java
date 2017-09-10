package com.testableapp.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.testableapp.R;
import com.testableapp.databinding.ActivityProfileBinding;
import com.testableapp.dto.User;
import com.testableapp.manager.AuthenticationManager;
import com.testableapp.presenters.ProfilePresenter;

public class ProfileActivity extends AbstractMvpActivity<ProfilePresenter> {

    private static final String EXTRA_USER = "extra_user";

    /**
     * Factory method
     *
     * @param context the application's context
     * @param user    the user data
     * @return the intent with necessary data for building the Activity
     */
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

        if (getIntent().getExtras() == null || !getIntent().getExtras().containsKey(EXTRA_USER)) {
            throw new IllegalStateException("ProfileActivity must be created using it's factory method");
        }

        final ActivityProfileBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_profile);

        final User user = (User) getIntent().getExtras().get(EXTRA_USER);
        binding.setUser(user);

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_profile;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        new MenuInflater(this).inflate(R.menu.activity_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (item.getItemId() == R.id.action_logout) {
            AuthenticationManager.getInstance().logOut(this);
            startActivity(new Intent(this, LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    protected ProfilePresenter createPresenter() {
        return new ProfilePresenter();
    }
}
