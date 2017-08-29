package com.testableapp.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.testableapp.R;
import com.testableapp.databinding.ProfileActivityBinding;
import com.testableapp.dto.User;

public class ProfileActivity extends AppCompatActivity {

    private static final String EXTRA_USER = "extra_user";

    public static Intent getIntent(@NonNull final Context context, @NonNull final User user) {
        final Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(EXTRA_USER, user);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        if (getIntent().getExtras().containsKey(EXTRA_USER)) {
            final ProfileActivityBinding binding = DataBindingUtil
                    .setContentView(this, R.layout.profile_activity);

            final User user = (User) getIntent().getExtras().get(EXTRA_USER);
            binding.setUser(user);

        } else {
            finish();
        }
    }
}
