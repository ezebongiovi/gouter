package com.testableapp.presenters;

import android.support.annotation.NonNull;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.RegistrationRequest;
import com.testableapp.dto.User;
import com.testableapp.models.AuthModel;
import com.testableapp.rx.ErrorConsumer;
import com.testableapp.views.RegisterView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RegisterPresenter extends AbstractPresenter<RegisterView> {

    public void register(@NonNull final String name, @NonNull final String lastName,
                         @NonNull final String email, @NonNull final String password,
                          @NonNull final String confirmation) {

        if (isValidEntry(email, password, confirmation)) {
            addDisposable(AuthModel.getInstance()
                    .register(new RegistrationRequest(name, lastName, email, password))
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<ApiResponse<User>>() {
                        @Override
                        public void accept(final ApiResponse<User> response) throws Exception {
                            if (ApiResponse.STATUS_OK.equalsIgnoreCase(response.status)) {
                                getView().onRegister(response.data);
                            } else {
                                getView().onError(response.message);
                            }
                        }
                    }, new ErrorConsumer() {
                        @Override
                        public void onError(@NonNull final ApiResponse apiResponse) {
                            handleErrorEvent(apiResponse);
                        }
                    }));

        } else {
            getView().onInvalidData();
        }

    }

    public boolean isValidEntry(@NonNull final String email, @NonNull final String password,
                                @NonNull final String confirmation) {
        return !email.isEmpty() && (confirmation.compareToIgnoreCase(password) == 0)
                && !password.isEmpty();
    }
}
