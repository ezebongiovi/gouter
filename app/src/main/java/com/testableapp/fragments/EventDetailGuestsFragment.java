package com.testableapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.testableapp.R;
import com.testableapp.activities.ProfileActivity;
import com.testableapp.adapters.FragmentPagerAdapter;
import com.testableapp.dto.GEvent;
import com.testableapp.dto.Guest;
import com.testableapp.widgets.GuestsView;

public class EventDetailGuestsFragment extends AbstractEventDetailFragment implements GuestsView.GuestClickListener {

    /**
     * Factory method
     *
     * @param event the event being shown
     * @return the fragment's abstraction containing event data
     */
    public static FragmentPagerAdapter.PageView getInstance(@NonNull final GEvent event) {
        final EventDetailGuestsFragment fragment = new EventDetailGuestsFragment();
        final Bundle args = new Bundle();
        args.putParcelable(EXTRA_EVENT, event);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        renderData(mEvent);
    }

    @Override
    protected void renderData(@NonNull final GEvent mEvent) {
        GuestsView guestsView = getView().findViewById(R.id.guestView);
        guestsView.init(mEvent.guests, this);
    }

    @Override
    public String getTitle(@NonNull final Context context) {
        final GEvent event = getArguments().getParcelable(EXTRA_EVENT);
        return String.format(context.getString(R.string.fragment_title_event_guests), event.guests.size());
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_event_guests;
    }

    @Override
    public void onClick(@NonNull final Guest guest) {
        startActivity(ProfileActivity.getIntent(getContext(), guest.user));
    }
}
