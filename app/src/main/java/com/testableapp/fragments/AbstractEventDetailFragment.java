package com.testableapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.testableapp.adapters.FragmentPagerAdapter;
import com.testableapp.dto.GEvent;
import com.testableapp.fragments.base.AbstractFragment;

public abstract class AbstractEventDetailFragment extends AbstractFragment
        implements FragmentPagerAdapter.PageView {

    protected static final String EXTRA_EVENT = "extra-event";
    protected GEvent mEvent;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getArguments().containsKey(EXTRA_EVENT)) {
            throw new AssertionError("This event should be instantiated using it's factory method");
        }

        mEvent = getArguments().getParcelable(EXTRA_EVENT);
    }

    protected abstract void renderData(@NonNull GEvent event);

    @Override
    public void update(@NonNull final GEvent event) {
        renderData(event);
    }
}
