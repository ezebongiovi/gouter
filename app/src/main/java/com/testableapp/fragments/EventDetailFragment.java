package com.testableapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.testableapp.R;
import com.testableapp.adapters.FragmentPagerAdapter;
import com.testableapp.dto.GEvent;

public class EventDetailFragment extends AbstractEventDetailFragment {

    /**
     * Factory method
     *
     * @param event the event being shown
     * @return the fragment's abstraction containing event data
     */
    public static FragmentPagerAdapter.PageView getInstance(@NonNull final GEvent event) {
        final EventDetailFragment fragment = new EventDetailFragment();
        final Bundle args = new Bundle();
        args.putParcelable(EXTRA_EVENT, event);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView) view.findViewById(R.id.eventDate)).setText(mEvent.date.toString());
        ((TextView) view.findViewById(R.id.eventDescription)).setText(mEvent.description);
    }

    @Override
    public int getTitle() {
        return R.string.fragment_title_event_detail;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_event_detail;
    }
}
