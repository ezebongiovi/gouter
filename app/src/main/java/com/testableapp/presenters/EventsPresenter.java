package com.testableapp.presenters;

import android.support.annotation.NonNull;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.GEvent;
import com.testableapp.models.EventsModel;
import com.testableapp.rx.ErrorConsumer;
import com.testableapp.views.EventsView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EventsPresenter extends AbstractPresenter<EventsView> {

    @Override
    public void attachView(@NonNull final EventsView view) {
        super.attachView(view);

        if (getCompositeDisposable().size() == 0) {

            addDisposable(EventsModel.getInstance().getEvents().observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).subscribe(new Consumer<ApiResponse<List<GEvent>>>() {
                        @Override
                        public void accept(final ApiResponse<List<GEvent>> listApiResponse) throws Exception {
                            if (ApiResponse.STATUS_OK.equalsIgnoreCase(listApiResponse.status)) {
                                getView().showEvents(listApiResponse.data);
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
}
