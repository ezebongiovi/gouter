package com.testableapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.testableapp.R;
import com.testableapp.adapters.FragmentPagerAdapter;
import com.testableapp.dto.GEvent;

public class EventDetailGuestsFragment extends AbstractEventDetailFragment {

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
    public int getTitle() {
        return R.string.fragment_title_event_guests;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_event_guests;
    }
}
