package com.testableapp.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.testableapp.R;
import com.testableapp.adapters.GuestsAdapter;
import com.testableapp.dto.Guest;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by epasquale on 27/11/17.
 */

public class GuestsView extends LinearLayout {

    private GuestsAdapter mAdapter;

    public GuestsView(final Context context) {
        this(context, null);
    }

    public GuestsView(final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuestsView(final Context context, @Nullable final AttributeSet attrs,
                      final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.widget_guest_view, this);
    }

    public void init(@NonNull final List<Guest> guestList) {
        final RecyclerView recyclerView = findViewById(R.id.guestList);
        final TextView searchField = findViewById(R.id.searchField);

        mAdapter = new GuestsAdapter(guestList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        RxTextView.afterTextChangeEvents(searchField).subscribe(new Consumer<TextViewAfterTextChangeEvent>() {
            @Override
            public void accept(final TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) throws Exception {
                Observable.fromIterable(guestList).filter(new Predicate<Guest>() {
                    @Override
                    public boolean test(Guest guest) throws Exception {
                        return guest.user.firstName.toLowerCase()
                                .contains(searchField.getText().toString().toLowerCase())
                                || guest.user.lastName.toLowerCase().contains(searchField.getText()
                                .toString().toLowerCase());
                    }
                }).toList().subscribe(new SingleObserver<List<Guest>>() {

                    @Override
                    public void onSubscribe(final Disposable d) {

                    }

                    @Override
                    public void onSuccess(final List<Guest> guests) {
                        mAdapter.setItems(guests);
                    }

                    @Override
                    public void onError(final Throwable e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}
