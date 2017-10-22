package com.testableapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ViewFlipper;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.testableapp.R;
import com.testableapp.activities.EventDetailActivity;
import com.testableapp.adapters.EventsAdapter;
import com.testableapp.adapters.PaginationAdapter;
import com.testableapp.adapters.holders.GenericViewHolder;
import com.testableapp.dto.GEvent;
import com.testableapp.fragments.base.AbstractMvpFragment;
import com.testableapp.presenters.EventsPresenter;
import com.testableapp.views.EventsView;

import java.util.List;

public class EventsFragment extends AbstractMvpFragment<EventsPresenter>
        implements EventsView, EventsAdapter.OnEventClick, PaginationAdapter.PaginationListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String EXTRA_TYPE = "EXTRA_TYPE";
    private static final int VIEW_PROGRESS = 0;
    private static final int VIEW_REGULAR = 1;
    private static final int VIEW_EMPTY = 2;

    private EventsAdapter mAdapter;
    private ViewFlipper mFlipper;
    private SwipeRefreshLayout mRefreshLayout;

    /**
     * Factory method for building fragment with necessary initialization data.
     *
     * @param events the type of events wanted to be shown
     *               see {@link com.testableapp.models.EventsModel#MY_EVENTS}
     * @return the fragment with necessary data
     */
    public static Fragment getInstance(final int events) {
        final Bundle args = new Bundle();
        args.putInt(EXTRA_TYPE, events);

        final EventsFragment fragment = new EventsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView listView = view.findViewById(R.id.eventsList);
        final LinearSnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
        mRefreshLayout = view.findViewById(R.id.refreshLayout);

        mRefreshLayout.setOnRefreshListener(this);
        snapHelper.attachToRecyclerView(listView);
        mFlipper = view.findViewById(R.id.viewFlipper);
        mAdapter = new EventsAdapter(this);
        mAdapter.attachTo(listView);
        mAdapter.setOnEventClick(this);
    }

    @NonNull
    @Override
    protected EventsPresenter createPresenter() {
        return new EventsPresenter(getArguments().getInt(EXTRA_TYPE));
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_events;
    }

    @Override
    public void showProgressLayout() {
        mFlipper.setDisplayedChild(VIEW_PROGRESS);
    }

    @Override
    public void showRegularLayout() {
        mRefreshLayout.setRefreshing(false);
        mFlipper.setDisplayedChild(VIEW_REGULAR);
    }

    @Override
    public void onGenericError() {

    }

    @Override
    public void showEvents(@NonNull final List<GEvent> data) {
        mAdapter.setItems(data);
    }

    @Override
    public void showListProgress() {
        mAdapter.showLoading();
    }

    @Override
    public void hideListProgress() {
        mAdapter.hideLoading();
    }

    @Override
    public void showEmptyState() {
        mFlipper.setDisplayedChild(VIEW_EMPTY);
    }

    @Override
    public void addEvents(@NonNull final List<GEvent> results) {
        mAdapter.addItems(results);
    }

    @Override
    public void onClick(@NonNull final GenericViewHolder viewHolder, @NonNull final GEvent event) {
        final View view = viewHolder.itemView;
        final View statusBar = getActivity().findViewById(android.R.id.statusBarBackground);
        final View navigationBar = getActivity().findViewById(android.R.id.navigationBarBackground);

        final Intent intent = EventDetailActivity.getIntent(getContext(), event);
        final Pair<View, String> p1 = Pair.create(view.findViewById(R.id.coverView), "coverView");
        final Pair<View, String> p2 = Pair.create(view.findViewById(R.id.eventDate), "date");
        final ActivityOptionsCompat options;

        if (statusBar != null && navigationBar != null) {
            final Pair<View, String> p3 = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
            final Pair<View, String> p4 = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);

            options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), p1, p2, p3, p4);
        } else {
            options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), p1, p2);
        }

        // Our NavigationActivity overrides startActivity method for animations
        getActivity().startActivity(intent, options.toBundle());
    }

    @Override
    public void onEndReached(final int offset) {
        mPresenter.getEvents(offset);
    }

    @Override
    public void onRefresh() {
        mPresenter.getEvents(0);
    }
}
