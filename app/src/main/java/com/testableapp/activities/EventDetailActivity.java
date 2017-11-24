package com.testableapp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.testableapp.R;
import com.testableapp.adapters.FragmentPagerAdapter;
import com.testableapp.dto.GEvent;
import com.testableapp.fragments.EventDetailFragment;
import com.testableapp.fragments.EventDetailGuestsFragment;
import com.testableapp.presenters.EmptyPresenter;

import java.util.ArrayList;
import java.util.List;

public class EventDetailActivity extends AbstractActivity {

    private static final String EXTRA_EVENT = "extra-event";
    private boolean mEditMode;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    public EventDetailActivity() {
        super(FLAG_ROOT_VIEW | FLAG_BACK_ARROW);
    }

    /**
     * Factory method
     *
     * @param context the application's context
     * @param event   the event's detail being loaded
     * @return the intent containing necessary data for launching the Activity
     */
    @NonNull
    public static Intent getIntent(@NonNull final Context context, @NonNull final GEvent event) {
        final Intent intent = new Intent(context, EventDetailActivity.class);
        intent.putExtra(EXTRA_EVENT, event);

        return intent;
    }

    @Override
    protected boolean shouldAuthenticate() {
        return true;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                 @NonNull final EmptyPresenter presenter) {
        final GEvent event = getIntent().getExtras().getParcelable(EXTRA_EVENT);

        if (getIntent().getExtras() == null || event == null
                || !getIntent().getExtras().containsKey(EXTRA_EVENT)) {
            throw new AssertionError("EventDetailActivity should be created using it's factory method");
        }

        mTabLayout = findViewById(R.id.tabLayout);

        initTabs(event);

        initOptions();

        final ImageView imageView = findViewById(R.id.coverView);
        Picasso.with(this).load(event.cover.url).into(imageView);

        setUpUI();

        final View editButton = findViewById(R.id.editButton);
        final Animation anim = AnimationUtils.loadAnimation(EventDetailActivity.this,
                R.anim.scale_in);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {
                editButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(final Animation animation) {

            }

            @Override
            public void onAnimationRepeat(final Animation animation) {

            }
        });

        editButton.startAnimation(anim);
        editButton.setOnClickListener(v -> editEvent());
    }

    private void initOptions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.editDateButton).setOnClickListener(v -> editDate());
        }
    }

    private void editDate() {
        final AlertDialog dialog = new AlertDialog.Builder(EventDetailActivity.this)
                .setView(R.layout.dialog_edit_date)
                .create();
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    private void handleBack() {
        if (mEditMode) {
            toggleEditMode();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                circularHide();
            } else {
                unReveal();
            }
        } else {
            finishAfterTransition();
        }
    }

    private void initTabs(@NonNull final GEvent event) {
        // Setup fragments
        final List<FragmentPagerAdapter.PageView> views = new ArrayList<>();
        views.add(EventDetailFragment.getInstance(event));
        views.add(EventDetailGuestsFragment.getInstance(event));

        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(new com.testableapp.adapters.FragmentPagerAdapter(EventDetailActivity.this,
                getSupportFragmentManager(), views));

        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setUpUI() {
        final Animation anim = AnimationUtils.loadAnimation(EventDetailActivity.this, R.anim.slide_up_in);
        anim.setInterpolator(new AccelerateDecelerateInterpolator(EventDetailActivity.this, null));
        findViewById(R.id.viewPager).startAnimation(anim);
    }

    @Override
    public void finishAfterTransition() {
        final Animation anim = AnimationUtils.loadAnimation(EventDetailActivity.this, R.anim.slide_up_out);
        anim.setInterpolator(new AccelerateDecelerateInterpolator(EventDetailActivity.this, null));
        findViewById(R.id.viewPager).startAnimation(anim);

        final Animation fabAnim = AnimationUtils.loadAnimation(EventDetailActivity.this, R.anim.scale_out);
        findViewById(R.id.editButton).startAnimation(fabAnim);

        super.finishAfterTransition();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            handleBack();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_event_detail;
    }

    private void editEvent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            circularReveal();
        } else {
            reveal();
        }
    }

    private void unReveal() {
        final View fab = findViewById(R.id.editButton);
        fab.setVisibility(View.VISIBLE);
        fab.startAnimation(AnimationUtils.loadAnimation(EventDetailActivity.this,
                R.anim.scale_fab_in));
        findViewById(R.id.optionsList).setVisibility(View.GONE);
    }

    private void toggleEditMode() {
        if (mEditMode) {
            // Show back arrow on Toolbar
            final AnimatedVectorDrawableCompat drawableCompat = AnimatedVectorDrawableCompat
                    .create(EventDetailActivity.this, R.drawable.from_close_to_back_arrow);

            getSupportActionBar().setHomeAsUpIndicator(drawableCompat);

            drawableCompat.start();
        } else {
            final AnimatedVectorDrawableCompat drawableCompat = AnimatedVectorDrawableCompat
                    .create(EventDetailActivity.this, R.drawable.from_arrow_to_close);

            getSupportActionBar().setHomeAsUpIndicator(drawableCompat);

            drawableCompat.start();
        }

        mEditMode = !mEditMode;
    }

    private void reveal() {
        final View fab = findViewById(R.id.editButton);
        final Animation anim = AnimationUtils.loadAnimation(EventDetailActivity.this,
                R.anim.scale_out);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(final Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                fab.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {

            }
        });
        fab.startAnimation(anim);
        toggleEditMode();
        findViewById(R.id.optionsList).setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void circularReveal() {
        final View optionsList = findViewById(R.id.optionsList);
        final View coverView = findViewById(R.id.coverView);

        final int cx = (optionsList.getLeft() + optionsList.getRight()) / 2;
        final int cy = ((optionsList.getBottom() - optionsList.getTop()) / 2)
                - ((coverView.getBottom() - coverView.getTop()) / 2);

        final int finalRadius = Math.max(optionsList.getWidth(), optionsList.getHeight());

        final Animator anim = ViewAnimationUtils.createCircularReveal(optionsList, cx, cy, 0, finalRadius);
        anim.setDuration(250);
        anim.setInterpolator(new AccelerateInterpolator(1));
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(final Animator animation) {
                toggleEditMode();
                optionsList.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                mViewPager.setVisibility(View.GONE);
            }
        });

        final View view = findViewById(R.id.editButton);
        final AnimatorSet set = new AnimatorSet();
        final ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "translationX",
                cx + view.getHeight() / 2);
        animatorX.setDuration(250);
        animatorX.setInterpolator(new AccelerateInterpolator(1));
        final ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "translationY",
                cy);
        animatorY.setDuration(250);
        animatorY.setInterpolator(new AccelerateDecelerateInterpolator());

        animatorY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final Animator animation) {
                view.setVisibility(View.GONE);
                anim.start();
            }
        });

        set.playTogether(animatorX, animatorY);
        set.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void circularHide() {
        final View optionsList = findViewById(R.id.optionsList);
        final View coverView = findViewById(R.id.coverView);

        final int cx = (optionsList.getLeft() + optionsList.getRight()) / 2;
        final int cy = ((optionsList.getBottom() - optionsList.getTop()) / 2)
                - ((coverView.getBottom() - coverView.getTop()) / 2);

        final int initialRadius = optionsList.getWidth();

        // FAB TRANSLATION
        final View fabButton = findViewById(R.id.editButton);
        final AnimatorSet set = new AnimatorSet();
        final ObjectAnimator animatorX = ObjectAnimator.ofFloat(fabButton, "translationX", 0);
        final ObjectAnimator animatorY = ObjectAnimator.ofFloat(fabButton, "translationY", 0);
        set.playTogether(animatorX, animatorY);
        set.start();

        final Animator anim =
                ViewAnimationUtils.createCircularReveal(optionsList, cx, cy, initialRadius, 0);
        anim.setDuration(250);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(final Animator animation) {
                super.onAnimationStart(animation);
                mViewPager.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                optionsList.setVisibility(View.INVISIBLE);
                fabButton.setVisibility(View.VISIBLE);
                fabButton.startAnimation(AnimationUtils
                        .loadAnimation(EventDetailActivity.this, R.anim.scale_fab_in));
            }
        });

        anim.start();
    }
}
