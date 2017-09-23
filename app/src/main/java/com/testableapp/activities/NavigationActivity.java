package com.testableapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.testableapp.R;
import com.testableapp.fragments.EventsFragment;
import com.testableapp.fragments.ProfileFragment;
import com.testableapp.presenters.EmptyPresenter;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AbstractActivity {

    private final List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected boolean shouldAuthenticate() {
        return true;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                 @NonNull final EmptyPresenter presenter) {

        mFragments.add(new EventsFragment());
        mFragments.add(new ProfileFragment());


        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigationView);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView
                .OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                if (item.getItemId() == R.id.action_settings) {
                    display(1);
                } else if (item.getItemId() == R.id.action_home) {
                    display(0);
                }

                return true;
            }
        });

        findViewById(R.id.floatMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivity(new Intent(NavigationActivity.this,
                        CreateEventActivity.class));
            }
        });

        display(0);
    }

    private void display(final int position) {
        if (position != 0) {

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.viewPager,
                mFragments.get(position)).commit();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_navigation;
    }
}
