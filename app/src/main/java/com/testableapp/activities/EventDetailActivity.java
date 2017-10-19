package com.testableapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.graphics.Palette;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.testableapp.R;
import com.testableapp.dto.GEvent;
import com.testableapp.presenters.EmptyPresenter;

public class EventDetailActivity extends AbstractActivity {

    private static final String EXTRA_EVENT = "extra-event";

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

        final String text = getString(R.string.lorem_ipsum_large) + getString(R.string.lorem_ipsum_large);

        ((TextView) findViewById(R.id.eventAddressView)).setText(event.address.formattedAddress);
        ((TextView) findViewById(R.id.eventDescription)).setText(text);
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
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateDecelerateInterpolator(EventDetailActivity.this, null));
        findViewById(R.id.nestedScrollView).startAnimation(anim);

        cpt.setContentScrim(predomColor);
        actionBar.setBackgroundDrawable(predomColor);
        actionBar.setTitle(spannable);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_event_detail;
    }
}
