package com.testableapp.presenters;

import android.support.annotation.NonNull;

import com.testableapp.dto.ApiResponse;
import com.testableapp.models.PlacesModel;
import com.testableapp.rx.ErrorConsumer;
import com.testableapp.views.PlacePickerView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PlacePickerPresenter {
    private final PlacePickerView view;
    private Disposable mDisposable;

    /**
     * Default constructor
     *
     * @param view the place picker's view abstraction
     */
    public PlacePickerPresenter(@NonNull final PlacePickerView view) {
        this.view = view;
    }

    public void search(@NonNull final String criteria) {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }

        mDisposable = PlacesModel.getInstance().getMockedPlaces(criteria)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(apiResponse -> {

                    if (ApiResponse.STATUS_OK.equals(apiResponse.status)) {
                        view.onSearchResult(apiResponse.data);
                    } else {
                        view.onError(apiResponse.message);
                    }
                }, new ErrorConsumer() {
                    @Override
                    public void onError(@NonNull final ApiResponse apiResponse) {
                        if (ApiResponse.Kind.NETWORK.equals(apiResponse.kind)) {
                            view.onNetworkError();
                        } else {
                            view.onGenericError();
                        }
                    }
                });
    }

    public void onViewDetached() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
