package com.testableapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
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
        Picasso.with(this).load(event.cover.url).into(imageView);

        final String text = getString(R.string.lorem_ipsum_large) + getString(R.string.lorem_ipsum_large);

        ((TextView) findViewById(R.id.eventAddressView)).setText(event.address.formattedAddress);
        ((TextView) findViewById(R.id.eventDescription)).setText(text);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_event_detail;
    }
}
