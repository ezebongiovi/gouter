package com.testableapp.presenters;

import android.support.annotation.NonNull;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.Authentication;
import com.testableapp.dto.User;
import com.testableapp.models.AuthModel;
import com.testableapp.rx.ErrorConsumer;
import com.testableapp.views.LoginView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends AbstractPresenter<LoginView> {

    public void login(@NonNull final String email, @NonNull final String password) {
        addDisposable(AuthModel.getInstance().login(new Authentication(email, password))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ApiResponse<User>>() {
                    @Override
                    public void accept(final ApiResponse<User> response) throws Exception {
                        if (ApiResponse.STATUS_OK.equalsIgnoreCase(response.status)) {
                            getView().onLogin(response.data);
                        }
                    }
                }, new ErrorConsumer() {
                    @Override
                    public void onError(@NonNull final ApiResponse apiResponse) {
                        handleErrorEvent(apiResponse);
                    }
                }));
    }
}
