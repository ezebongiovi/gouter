package com.testableapp.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.testableapp.R;
import com.testableapp.adapters.PlacePredictionAdapter;
import com.testableapp.dto.Place;
import com.testableapp.presenters.PlacePickerPresenter;
import com.testableapp.views.PlacePickerView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PlacePicker extends FrameLayout implements PlacePickerView {

    private final PlacePickerPresenter mPresenter = new PlacePickerPresenter(this);
    private final PlacePredictionAdapter mAdapter;
    @VisibleForTesting
    public static final long SEARCH_DEBOUNCE = 500;
    private Disposable mTextWatcher;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private EditText mSearchField;
    private boolean isListShown;
    private boolean mTextWatcherFlag = true;
    private Place mSelectedPlace;

    public PlacePicker(@NonNull final Context context) {
        this(context, null);
    }

    public PlacePicker(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlacePicker(@NonNull final Context context, @Nullable final AttributeSet attrs,
                       final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final View view = LayoutInflater.from(context)
                .inflate(R.layout.widget_place_picker, this);

        final RecyclerView recyclerView = view.findViewById(R.id.placePickerList);
        mAdapter = new PlacePredictionAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        mMapView = view.findViewById(R.id.map);
        mSearchField = view.findViewById(R.id.searchField);

        mMapView.onCreate(null);
        mMapView.getMapAsync(this);

        mTextWatcher = initTextWatcher();

        mSearchField.setOnClickListener(v -> showList());
    }

    private Disposable initTextWatcher() {
        return RxTextView.afterTextChangeEvents(mSearchField)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(charSequence -> {
                    if (!mTextWatcherFlag) {
                        mTextWatcherFlag = true;
                        return;
                    }

                    final String criteria = charSequence.toString().trim();

                    if (criteria.isEmpty()) {
                        if (mSearchField.getText().toString().isEmpty()) {
                            hideList();
                        } else {
                            mAdapter.clear();
                        }
                    } else {
                        mPresenter.search(criteria);
                    }
                });
    }

    public Place getSelectedPlace() {
        return mSelectedPlace;
    }

    private void hideList() {
        if (!isListShown) {
            return;
        }

        final View view = findViewById(R.id.searchListBackground);
        final Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_out);
        anim.setDuration(250);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                isListShown = false;
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {

            }
        });
        view.startAnimation(anim);
    }

    private void showList() {
        if (isListShown) {
            return;
        }

        final View view = findViewById(R.id.searchListBackground);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(AnimationUtils
                .loadAnimation(getContext(), R.anim.slide_up_in));

        isListShown = true;
    }

    @Override
    public void onSearchResult(@NonNull final List<Place> data) {
        showList();
        mAdapter.setItems(data);
    }

    public void onError(final String string) {
        Snackbar.make(this, string, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onNetworkError() {
        onError("Network error");
    }

    @Override
    public void onGenericError() {
        onError("Generic error");
    }

    @Override
    public void onPlaceSelected(@NonNull final Place place) {
        mTextWatcherFlag = false;
        mSearchField.setText(place.formattedAddress);
        mSearchField.setSelection(place.formattedAddress.length());

        mSelectedPlace = place;
        hideList();
        mGoogleMap.clear();


        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place.coords);

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.coords, 17));

        mGoogleMap.addMarker(markerOptions);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mGoogleMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mMapView.onStart();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mMapView.onDestroy();
        mPresenter.onViewDetached();
    }
}
