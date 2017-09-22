package com.testableapp.presenters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.Search;
import com.testableapp.dto.User;
import com.testableapp.models.UsersModel;
import com.testableapp.rx.ErrorConsumer;
import com.testableapp.views.ContactPickerView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ContactPickerPresenter extends AbstractPresenter<ContactPickerView> {

    private Search.Pagination mPagination;
    private static final int limit = 6;
    private Disposable mSubscription;

    public void search(@Nullable final String criteria, final int offset) {
        if (mPagination != null && offset >= mPagination.total) {
            return;
        }

        if (offset == 0) {
            getView().showProgressLayout();
        } else {
            getView().showListLoading();
        }

        if (mSubscription != null) {
            getCompositeDisposable().remove(mSubscription);
            mSubscription = null;
        }

        mSubscription = UsersModel.getInstance().searchPeople(criteria, offset, limit)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ApiResponse<Search<User>>>() {

                    @Override
                    public void accept(final ApiResponse<Search<User>> listApiResponse)
                            throws Exception {
                        if (ApiResponse.STATUS_OK.equalsIgnoreCase(listApiResponse.status)) {
                            mPagination = listApiResponse.data.paging;

                            if (offset == 0) {
                                getView().showRegularLayout();
                                getView().loadContacts(listApiResponse.data.results);
                            } else {
                                getView().hideListLoading();
                                getView().addContacts(listApiResponse.data.results);
                            }
                        } else {
                            getView().onError(listApiResponse.message);
                        }
                    }
                }, new ErrorConsumer() {
                    @Override
                    public void onError(@NonNull final ApiResponse apiResponse) {
                        getView().onError(apiResponse.message);
                    }
                });

        addDisposable(mSubscription);
    }
}
