package com.testableapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.testableapp.R;
import com.testableapp.fragments.EventsFragment;
import com.testableapp.fragments.ProfileFragment;
import com.testableapp.models.EventsModel;
import com.testableapp.presenters.EmptyPresenter;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AbstractActivity {

    private final List<Page> mPages = new ArrayList<>();
    private boolean shouldAnimateBack;

    public NavigationActivity() {
        super(FLAG_HIDE_TOOLBAR);
    }

    @Override
    protected boolean shouldAuthenticate() {
        return true;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                 @NonNull final EmptyPresenter presenter) {

        mPages.add(new Page(EventsFragment.getInstance(EventsModel.EVENTS), getString(R.string.events)));
        mPages.add(new Page(EventsFragment.getInstance(EventsModel.MY_EVENTS), getString(R.string.my_events)));
        mPages.add(new Page(new ProfileFragment(), getString(R.string.my_account)));


        final BottomNavigationView navigation = findViewById(R.id.navigationView);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView
                .OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                if (item.getItemId() == R.id.action_settings) {
                    display(2);
                } else if (item.getItemId() == R.id.action_events) {
                    display(0);
                } else if (item.getItemId() == R.id.action_my_events) {
                    display(1);
                }

                return true;
            }
        });

        findViewById(R.id.button_create_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivity(new Intent(NavigationActivity.this,
                        CreateEventActivity.class));
            }
        });

        display(0);
    }

    private void display(final int position) {
        // Shows or hides float button
        findViewById(R.id.button_create_event).setVisibility(position == 0 || position == 1
                ? View.VISIBLE : View.GONE);

        final Page page = mPages.get(position);
        setTitle(page.title);

        getSupportFragmentManager().beginTransaction().replace(R.id.viewPager, page.fragment).commit();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_navigation;
    }

    private static final class Page {
        final Fragment fragment;
        final String title;

        Page(final Fragment fragment, final String title) {
            this.title = title;
            this.fragment = fragment;
        }
    }

    public void startActivityWithAnimation(@NonNull final Intent intent,
                                           @Nullable final Bundle options) {
        shouldAnimateBack = true;
        final Animation anim = AnimationUtils.loadAnimation(NavigationActivity.this,
                R.anim.slide_up_out);
        anim.setFillAfter(true);
        anim.setInterpolator(new AccelerateInterpolator(2));
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {
                NavigationActivity.super.startActivity(intent, options);
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {
            }
        });

        findViewById(R.id.button_create_event).startAnimation(anim);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (shouldAnimateBack) {
            shouldAnimateBack = false;
            final Animation anim = AnimationUtils.loadAnimation(NavigationActivity.this,
                    R.anim.slide_up_in);
            anim.setFillAfter(true);
            findViewById(R.id.button_create_event).startAnimation(anim);
        }
    }
}
