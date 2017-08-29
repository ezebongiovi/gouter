package com.testableapp.presenters;

import android.support.annotation.NonNull;

import com.testableapp.dto.Authentication;
import com.testableapp.dto.User;
import com.testableapp.models.AuthModel;
import com.testableapp.views.LoginView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends AbstractPresenter<LoginView> {

    public void login(@NonNull final String userName, @NonNull final String password) {
        addDisposable(AuthModel.getInstance().login(new Authentication(userName, password))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(final User user) throws Exception {
                        getView().onLogin(user);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) throws Exception {
                        getView().onError();
                    }
                }));
    }
}
