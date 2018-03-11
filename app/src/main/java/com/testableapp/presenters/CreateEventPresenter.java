package com.testableapp.presenters;

import android.support.annotation.NonNull;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.CreateEvent;
import com.testableapp.dto.GEvent;
import com.testableapp.models.EventsModel;
import com.testableapp.rx.ErrorConsumer;
import com.testableapp.views.CreateEventView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CreateEventPresenter extends AbstractPresenter<CreateEventView> {

    public void createEvent(final GEvent gEvent) {

        getView().showProgressLayout();

        addDisposable(EventsModel.getInstance().createEvent(gEvent.coverFile, new CreateEvent(gEvent))
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ApiResponse<GEvent>>() {
                    @Override
                    public void accept(final ApiResponse<GEvent> listApiResponse) {
                        if (ApiResponse.STATUS_OK.equalsIgnoreCase(listApiResponse.status)) {
                            getView().onEventCreated(listApiResponse.data);
                        } else {
                            getView().onError(listApiResponse.message);
                        }
                    }
                }, new ErrorConsumer() {
                    @Override
                    public void onError(@NonNull final ApiResponse apiResponse) {
                        getView().onGenericError();
                    }
                }));
    }
}
