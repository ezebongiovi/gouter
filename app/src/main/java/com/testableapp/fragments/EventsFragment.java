package com.testableapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testableapp.R;
import com.testableapp.adapters.EventsAdapter;
import com.testableapp.adapters.PaginationAdapter;
import com.testableapp.dto.GEvent;
import com.testableapp.fragments.base.AbstractMvpFragment;
import com.testableapp.presenters.EventsPresenter;
import com.testableapp.views.EventsView;

import java.util.Collections;
import java.util.List;

public class EventsFragment extends AbstractMvpFragment<EventsPresenter>
        implements EventsView, EventsAdapter.OnEventClick, PaginationAdapter.PaginationListener {

    private EventsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView listView = (RecyclerView) view.findViewById(R.id.eventsList);
        mAdapter = new EventsAdapter(Collections.EMPTY_LIST, this);
        mAdapter.attachTo(listView);

        mPresenter.getEvents(0);
    }

    @NonNull
    @Override
    protected EventsPresenter createPresenter() {
        return new EventsPresenter();
    }

    @Override
    public void showProgressLayout() {
        getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.eventsList).setVisibility(View.GONE);
    }

    @Override
    public void showRegularLayout() {
        getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
        getView().findViewById(R.id.eventsList).setVisibility(View.VISIBLE);
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
    public void addEvents(@NonNull final List<GEvent> results) {
        mAdapter.addItems(results);
    }

    @Override
    public void onClick(@NonNull final GEvent event) {
        // TODO: Handle event click
    }

    @Override
    public void onEndReached(final int offset) {
        mPresenter.getEvents(offset);
    }
}
