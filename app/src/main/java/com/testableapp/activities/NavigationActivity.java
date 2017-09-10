package com.testableapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.testableapp.R;
import com.testableapp.presenters.EmptyPresenter;

public class NavigationActivity extends AbstractActivity {

    @Override
    protected boolean shouldAuthenticate() {
        return true;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                 @NonNull final EmptyPresenter presenter) {

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigationView);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView
                .OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                if (item.getItemId() == R.id.action_settings) {
                    // TODO: Go To Settings

                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_navigation;
    }
}
