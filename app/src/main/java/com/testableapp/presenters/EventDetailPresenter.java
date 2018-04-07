package com.testableapp.presenters;

import android.support.annotation.NonNull;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.GEvent;
import com.testableapp.dto.Guest;
import com.testableapp.dto.User;
import com.testableapp.models.EventsModel;
import com.testableapp.rx.ErrorConsumer;
import com.testableapp.views.EventDetailView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by epasquale on 28/11/17.
 */

public class EventDetailPresenter extends AbstractPresenter<EventDetailView> {

    public void switchAssistance(final User user, final GEvent event) {
        getView().showProgressLayout();

        for (final Guest guest : event.guests) {
            if (guest.user._id.equals(user._id)) {
                addDisposable(EventsModel.getInstance().switchAssistance(event, guest)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<ApiResponse>() {
                            @Override
                            public void accept(final ApiResponse apiResponse) throws Exception {
                                // TODO: Analizar como hacemos para saber en que estado quedo la asistencia
                                if (apiResponse.status.equals(ApiResponse.STATUS_OK)) {
                                    if (Guest.STATUS_ACCEPTED.equals(getNewStatus(guest))) {
                                        getView().onInvitationAccepted();
                                    } else {
                                        getView().onInvitationRejected();
                                    }
                                }
                            }
                        }, new ErrorConsumer() {
                            @Override
                            public void onError(@NonNull final ApiResponse apiResponse) {
                                if (ApiResponse.Kind.NETWORK.equals(apiResponse.kind)) {
                                    getView().onNetworkError();
                                } else {
                                    getView().onGenericError();
                                }
                            }
                        }));

                break;
            }
        }
    }

    private String getNewStatus(final Guest guest) {
        if (Guest.STATUS_ACCEPTED.equals(guest.status)) {
            return Guest.STATUS_REJECTED;
        } else {
            return Guest.STATUS_ACCEPTED;
        }
    }

    public void edit(@NonNull final GEvent oldData, @NonNull final GEvent newData) {
        final GEvent data = new GEvent.Builder(oldData).copyData(newData).build();

        addDisposable(EventsModel.getInstance().editEvent(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ApiResponse>() {
                    @Override
                    public void accept(final ApiResponse apiResponse) throws Exception {
                        // TODO: Analizar como hacemos para saber en que estado quedo la asistencia
                        if (apiResponse.status.equals(ApiResponse.STATUS_OK)) {
                            getView().onEditionSuccess(data);
                        }
                    }
                }, new ErrorConsumer() {
                    @Override
                    public void onError(@NonNull final ApiResponse apiResponse) {
                        if (ApiResponse.Kind.NETWORK.equals(apiResponse.kind)) {
                            getView().onNetworkError();
                        } else {
                            getView().onGenericError();
                        }
                    }
                }));
    }
}
