package com.testableapp.presenters;

import android.support.annotation.NonNull;

import com.testableapp.dto.ApiError;
import com.testableapp.dto.Authentication;
import com.testableapp.dto.User;
import com.testableapp.rx.ErrorConsumer;
import com.testableapp.views.RegisterView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RegisterPresenter extends AbstractPresenter<RegisterView> {

    public Observable<User> register(@NonNull final String email,
                                               @NonNull final String password,
                                               @NonNull final String confirmation) {
        if (isValidEntry(email, password, confirmation)) {
            final Observable<User> observable = Observable.just(new User("Ezequiel",
                    "Di Pasquale", new Authentication.Builder().withEmail("ezebongiovi@gmail.com")
                    .withToken("12738172").build()));

            observable.observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<User>() {
                        @Override
                        public void accept(final User user) throws Exception {
                            getView().onRegister(user);
                        }
                    }, new ErrorConsumer() {
                        @Override
                        public void onError(@NonNull final ApiError apiError) {
                            handleErrorEvent(apiError);
                        }
                    });
            return observable;
        } else {
            getView().onInvalidData();
        }

        return null;
    }

    public boolean isValidEntry(@NonNull final String email, @NonNull final String password,
                                 @NonNull final String confirmation) {
        return !email.isEmpty() && (confirmation.compareToIgnoreCase(password) == 0)
                && !password.isEmpty();
    }
}
