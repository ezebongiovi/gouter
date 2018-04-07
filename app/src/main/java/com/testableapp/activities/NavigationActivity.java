package com.testableapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.testableapp.R;
import com.testableapp.fragments.ContactsFragment;
import com.testableapp.fragments.EventsFragment;
import com.testableapp.fragments.ProfileFragment;
import com.testableapp.manager.AuthenticationManager;
import com.testableapp.models.EventsModel;
import com.testableapp.presenters.EmptyPresenter;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AbstractActivity {

    private final List<Page> mPages = new ArrayList<>();
    private boolean shouldAnimateBack;

    public NavigationActivity() {
        super(FLAG_NONE);
    }

    @Override
    protected boolean shouldAuthenticate() {
        return true;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                 @NonNull final EmptyPresenter presenter) {
        setTitle(getString(R.string.app_name));

        mPages.add(new Page(EventsFragment.getInstance(EventsModel.EVENTS), getString(R.string.events)));
        mPages.add(new Page(EventsFragment.getInstance(EventsModel.MY_EVENTS), getString(R.string.my_events)));
        mPages.add(new Page(new ContactsFragment(), getString(R.string.title_contacts)));
        mPages.add(new Page(ProfileFragment.getInstance(AuthenticationManager.getInstance()
                .getUser(NavigationActivity.this)), getString(R.string.my_account)));


        final BottomNavigationView navigation = findViewById(R.id.navigationView);
        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    display(3);
                    break;
                case R.id.action_events:
                    display(0);
                    break;
                case R.id.action_my_events:
                        display(1);
                        break;
                case R.id.action_contacts:
                    display(2);
                    break;
            }
            return true;
        });

        findViewById(R.id.button_create_event).setOnClickListener(v -> startActivity(
                new Intent(NavigationActivity.this, CreateEventActivity.class)));

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
