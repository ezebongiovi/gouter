package com.testableapp.presenters;

import android.support.annotation.NonNull;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.GEvent;
import com.testableapp.dto.Search;
import com.testableapp.models.EventsModel;
import com.testableapp.rx.ErrorConsumer;
import com.testableapp.views.EventsView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EventsPresenter extends AbstractPresenter<EventsView> {

    private Search.Pagination pagination;
    private static final int PAGE_LIMIT = 10;
    private Disposable mDisposable;

    public void getEvents(final int offset) {

        if (pagination != null && pagination.total <= offset) {
            return;
        }

        if (offset == 0) {
            getView().showProgressLayout();
        } else {
            getView().showListProgress();
        }

        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }

        mDisposable = EventsModel.getInstance().getEvents(offset, PAGE_LIMIT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Consumer<ApiResponse<Search<GEvent>>>() {
                    @Override
                    public void accept(final ApiResponse<Search<GEvent>> listApiResponse) throws Exception {
                        if (ApiResponse.STATUS_OK.equalsIgnoreCase(listApiResponse.status)) {
                            pagination = listApiResponse.data.paging;

                            if (offset == 0) {
                                getView().showRegularLayout();
                                getView().showEvents(listApiResponse.data.results);
                            } else {
                                getView().hideListProgress();
                                getView().addEvents(listApiResponse.data.results);
                            }
                        } else {
                            getView().onError(listApiResponse.message);
                        }
                    }
                }, new ErrorConsumer() {
                    @Override
                    public void onError(@NonNull final ApiResponse apiResponse) {
                        getView().onGenericError();
                    }
                });

        addDisposable(mDisposable);
    }
}
