package com.testableapp.presenters;

import android.support.annotation.NonNull;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.CreateEvent;
import com.testableapp.dto.GEvent;
import com.testableapp.dto.User;
import com.testableapp.models.EventsModel;
import com.testableapp.rx.ErrorConsumer;
import com.testableapp.views.CreateEventView;

import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CreateEventPresenter extends AbstractPresenter<CreateEventView> {

    public void createEvent(@NonNull final String authorId, @NonNull final String address,
                            @NonNull final Date date, @NonNull final String description,
                            @NonNull final List<User> guests) {

        final GEvent gEvent = new GEvent.Builder().setAddress(address).setDate(date)
                .setDescription(description).setGuests(guests).build();

        addDisposable(EventsModel.getInstance().createEvent(new CreateEvent(authorId, gEvent))
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ApiResponse<GEvent>>() {
                    @Override
                    public void accept(final ApiResponse<GEvent> listApiResponse)
                            throws Exception {
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
