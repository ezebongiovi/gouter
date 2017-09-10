package com.testableapp.activities;

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
import com.testableapp.manager.AuthenticationManager;
import com.testableapp.presenters.ProfilePresenter;

public class ProfileActivity extends AbstractMvpActivity<ProfilePresenter> {

    @Override
    protected boolean shouldAuthenticate() {
        return true;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                 final @NonNull ProfilePresenter presenter) {

        final ActivityProfileBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_profile);

        binding.setUser(AuthenticationManager.getInstance().getUser(this));

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
