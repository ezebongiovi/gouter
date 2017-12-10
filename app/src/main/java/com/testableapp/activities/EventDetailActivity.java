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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.testableapp.R;
import com.testableapp.adapters.FragmentPagerAdapter;
import com.testableapp.dto.GEvent;
import com.testableapp.fragments.EventDetailFragment;
import com.testableapp.fragments.EventDetailGuestsFragment;
import com.testableapp.manager.AuthenticationManager;
import com.testableapp.presenters.EventDetailPresenter;
import com.testableapp.views.EventDetailView;

import java.util.ArrayList;
import java.util.List;

public class EventDetailActivity extends AbstractMvpActivity<EventDetailPresenter>
        implements EventDetailView {

    private static final String EXTRA_EVENT = "extra-event";
    private boolean mEditMode;
    private FloatingActionButton mFloatingActionButton;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private GEvent mEvent;

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
    public void onCreateActivity(@Nullable final Bundle savedInstanceState, @NonNull final EventDetailPresenter presenter) {

        mEvent = getIntent().getExtras().getParcelable(EXTRA_EVENT);

        if (getIntent().getExtras() == null || mEvent == null
                || !getIntent().getExtras().containsKey(EXTRA_EVENT)) {
            throw new AssertionError("EventDetailActivity should be created using it's factory method");
        }

        mTabLayout = findViewById(R.id.tabLayout);

        initTabs();

        final FloatingActionButton editButton = findViewById(R.id.editButton);

        // Decides whether to show the assist button or the edit button
        if (isAuthor()) {
            mFloatingActionButton = editButton;
        } else {
            mFloatingActionButton = findViewById(R.id.assistButton);
        }

        mFloatingActionButton.setVisibility(View.VISIBLE);

        final ImageView imageView = findViewById(R.id.coverView);
        Picasso.with(this).load(mEvent.cover.url).into(imageView);

        setUpUI();

        final Animation anim = AnimationUtils.loadAnimation(EventDetailActivity.this,
                R.anim.scale_in);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {
                mFloatingActionButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(final Animation animation) {

            }

            @Override
            public void onAnimationRepeat(final Animation animation) {

            }
        });

        mFloatingActionButton.startAnimation(anim);
        mFloatingActionButton.setOnClickListener(v -> handleFabClick());
    }

    private void handleFabClick() {
        if (isAuthor()) {
            editEvent();
        } else {
            mPresenter.switchAssistance(AuthenticationManager.getInstance()
                    .getUser(EventDetailActivity.this), mEvent);
        }
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

    private void initTabs() {
        // Setup fragments
        final List<FragmentPagerAdapter.PageView> views = new ArrayList<>();
        views.add(EventDetailFragment.getInstance(mEvent));
        views.add(EventDetailGuestsFragment.getInstance(mEvent));

        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(new FragmentPagerAdapter(EventDetailActivity.this,
                getSupportFragmentManager(), views));
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 0) {
                    final View view = EventDetailActivity.this.getCurrentFocus();
                    if (view != null) {
                        final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        });

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

    @NonNull
    @Override
    protected EventDetailPresenter createPresenter() {
        return new EventDetailPresenter();
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

    /**
     * Called when edit description button has been clicked
     *
     * @param view the clicked view
     */
    public void editDescription(final View view) {

    }

    /**
     * Called when edit date button has been clicked
     *
     * @param view the clicked view
     */
    public void editDate(final View view) {

    }

    /**
     * Called when edit guests button has been clicked
     *
     * @param view the clicked view
     */
    public void editGuests(final View view) {

    }

    /**
     * Called when edit address button has been clicked
     *
     * @param view the clicked view
     */
    public void editAddress(final View view) {

    }

    public boolean isAuthor() {
        return mEvent.author._id.equals(AuthenticationManager.getInstance()
                .getUser(EventDetailActivity.this)._id);
    }

    @Override
    public void onInvitationAccepted() {
        // TODO: Animate from check to cross
        final AnimatedVectorDrawableCompat drawableCompat = AnimatedVectorDrawableCompat
                .create(EventDetailActivity.this, R.drawable.from_close_to_back_arrow);

        // TODO: Shoot event for updating event's guest list
    }

    @Override
    public void onInvitationRejected() {
        // TODO: Animate from cross to check
        final AnimatedVectorDrawableCompat drawableCompat = AnimatedVectorDrawableCompat
                .create(EventDetailActivity.this, R.drawable.from_close_to_back_arrow);

        // TODO: Shoot event for updating event's guest list
    }

    @Override
    public void showProgressLayout() {
        // TODO: Handle progress
    }
}
