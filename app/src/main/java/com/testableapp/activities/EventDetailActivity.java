package com.testableapp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.graphics.Palette;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.testableapp.R;
import com.testableapp.dto.GEvent;
import com.testableapp.presenters.EmptyPresenter;

public class EventDetailActivity extends AbstractActivity {

    private static final String EXTRA_EVENT = "extra-event";
    private boolean mEditMode;

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

        final ImageView imageView = findViewById(R.id.coverView);
        final ActionBar actionBar = getSupportActionBar();

        Picasso.with(this).load(event.cover.url).into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, final Picasso.LoadedFrom from) {
                imageView.setImageBitmap(bitmap);
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(final Palette palette) {
                        setUpUI(actionBar, palette, event);
                    }
                });
            }

            @Override
            public void onBitmapFailed(final Drawable errorDrawable) {
                // Nothing to do
            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {

            }
        });

        ((TextView) findViewById(R.id.eventDate)).setText(event.date.toString());
        ((TextView) findViewById(R.id.eventDescription)).setText(event.description);

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
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                editEvent();
            }
        });
    }

    private void setUpUI(final ActionBar actionBar, final Palette palette, final GEvent event) {
        final CollapsingToolbarLayout cpt = findViewById(R.id.collapsingToolbar);

        final ColorDrawable predomColor = new ColorDrawable(palette
                .getVibrantColor(getResources().getColor(R.color.colorPrimary)));

        final Spannable spannable = new SpannableString(event.author.firstName);
        spannable.setSpan(new ForegroundColorSpan(palette
                        .getLightVibrantColor(getResources().getColor(android.R.color.white))),
                0, event.author.firstName.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(palette.getVibrantColor(getResources()
                    .getColor(R.color.colorPrimary)));
        }

        final Animation anim = AnimationUtils.loadAnimation(EventDetailActivity.this, R.anim.slide_up_in);
        anim.setInterpolator(new AccelerateDecelerateInterpolator(EventDetailActivity.this, null));
        findViewById(R.id.nestedScrollView).startAnimation(anim);

        cpt.setContentScrim(predomColor);
        actionBar.setBackgroundDrawable(predomColor);
        actionBar.setTitle(spannable);
    }

    @Override
    public void finishAfterTransition() {
        final Animation anim = AnimationUtils.loadAnimation(EventDetailActivity.this, R.anim.slide_up_out);
        anim.setInterpolator(new AccelerateDecelerateInterpolator(EventDetailActivity.this, null));
        findViewById(R.id.nestedScrollView).startAnimation(anim);

        final Animation fabAnim = AnimationUtils.loadAnimation(EventDetailActivity.this, R.anim.scale_out);
        findViewById(R.id.editButton).startAnimation(fabAnim);

        super.finishAfterTransition();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
        findViewById(R.id.editContainer).setVisibility(View.GONE);
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
        findViewById(R.id.editContainer).setVisibility(View.VISIBLE);
        showOptions();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void circularReveal() {
        final View editContainer = findViewById(R.id.editContainer);
        final View dataContainer = findViewById(R.id.nestedScrollView);
        final View coverView = findViewById(R.id.coverView);

        final int cx = (editContainer.getLeft() + editContainer.getRight()) / 2;
        final int cy = ((editContainer.getBottom() - editContainer.getTop()) / 2)
                - ((coverView.getBottom() - coverView.getTop()) / 2);

        final int finalRadius = Math.max(editContainer.getWidth(), editContainer.getHeight());

        final Animator anim = ViewAnimationUtils.createCircularReveal(editContainer, cx, cy, 0, finalRadius);
        anim.setDuration(250);
        anim.setInterpolator(new AccelerateInterpolator(1));
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(final Animator animation) {
                editContainer.setVisibility(View.VISIBLE);
                showOptions();
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                dataContainer.setVisibility(View.GONE);
            }
        });

        final View view = findViewById(R.id.editButton);
        final AnimatorSet set = new AnimatorSet();
        final ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "translationX",
                -cx + view.getHeight() / 2);
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
        final View editContainer = findViewById(R.id.editContainer);
        final View coverView = findViewById(R.id.coverView);
        final View contentContainer = findViewById(R.id.nestedScrollView);

        final int cx = (editContainer.getLeft() + editContainer.getRight()) / 2;
        final int cy = ((editContainer.getBottom() - editContainer.getTop()) / 2)
                - ((coverView.getBottom() - coverView.getTop()) / 2);

        final int initialRadius = editContainer.getWidth();

        // FAB TRANSLATION
        final View fabButton = findViewById(R.id.editButton);
        final AnimatorSet set = new AnimatorSet();
        final ObjectAnimator animatorX = ObjectAnimator.ofFloat(fabButton, "translationX", 0);
        final ObjectAnimator animatorY = ObjectAnimator.ofFloat(fabButton, "translationY",0);
        set.playTogether(animatorX, animatorY);
        set.start();

        final Animator anim =
                ViewAnimationUtils.createCircularReveal(editContainer, cx, cy, initialRadius, 0);
        anim.setDuration(250);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(final Animator animation) {
                super.onAnimationStart(animation);
                contentContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                editContainer.setVisibility(View.INVISIBLE);
                fabButton.setVisibility(View.VISIBLE);
                fabButton.startAnimation(AnimationUtils
                        .loadAnimation(EventDetailActivity.this, R.anim.scale_fab_in));
            }
        });

        anim.start();
    }

    private void showOptions() {
        final Animation anim = AnimationUtils.loadAnimation(EventDetailActivity.this, R.anim.slide_fade_in);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        findViewById(R.id.optionsContainer).setVisibility(View.VISIBLE);

        findViewById(R.id.editDateButton).startAnimation(anim);
        anim.setStartOffset(250);
        anim.setInterpolator(new DecelerateInterpolator(1));
        findViewById(R.id.editAddressButton).startAnimation(anim);
        anim.setStartOffset(375);
        anim.setInterpolator(new DecelerateInterpolator(2));
        findViewById(R.id.editGuestsButton).startAnimation(anim);

        toggleEditMode();
    }
}
