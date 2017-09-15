package com.testableapp.presenters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.User;
import com.testableapp.models.UsersModel;
import com.testableapp.rx.ErrorConsumer;
import com.testableapp.views.ContactPickerView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ContactPickerPresenter extends AbstractPresenter<ContactPickerView> {

    @Override
    public void attachView(@NonNull final ContactPickerView view) {
        super.attachView(view);
        search(null);
    }

    public void search(@Nullable final String criteria) {
        getView().showProgressLayout();

        addDisposable(UsersModel.getInstance().searchPeople(criteria)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ApiResponse<List<User>>>() {
                    @Override
                    public void accept(final ApiResponse<List<User>> listApiResponse)
                            throws Exception {
                        getView().showRegularLayout();
                        if (ApiResponse.STATUS_OK.equalsIgnoreCase(listApiResponse.status)) {
                            getView().loadContacts(listApiResponse.data);
                        } else {
                            getView().onError(listApiResponse.message);
                        }
                    }
                }, new ErrorConsumer() {
                    @Override
                    public void onError(@NonNull final ApiResponse apiResponse) {
                        getView().onError(apiResponse.message);
                    }
                }));
    }
}
