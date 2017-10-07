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

    private final int mEventType;
    private Search<GEvent> mData;
    private static final int PAGE_LIMIT = 10;
    private Disposable mDisposable;

    public EventsPresenter(final int eventType) {
        mEventType = eventType;
    }

    @Override
    public void attachView(@NonNull final EventsView view) {
        super.attachView(view);

        if (mData == null) {
            getEvents(0);
        } else {
            getView().showEvents(mData.results);
        }
    }

    public void getEvents(final int offset) {

        if (mData != null && mData.paging.total <= offset) {
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

        mDisposable = EventsModel.getInstance().getEvents(mEventType, offset, PAGE_LIMIT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Consumer<ApiResponse<Search<GEvent>>>() {
                    @Override
                    public void accept(final ApiResponse<Search<GEvent>> listApiResponse) throws Exception {
                        if (ApiResponse.STATUS_OK.equalsIgnoreCase(listApiResponse.status)) {
                            mData = listApiResponse.data;

                            if (offset == 0 && listApiResponse.data.results.isEmpty()) {
                                getView().showEmptyState();
                                return;
                            }

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
