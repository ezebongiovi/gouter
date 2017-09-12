package com.testableapp.models;

import android.support.annotation.NonNull;

import com.testableapp.MainApplication;
import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.Authentication;
import com.testableapp.dto.User;
import com.testableapp.services.AuthService;

import io.reactivex.Observable;

import static com.testableapp.dto.ApiResponse.STATUS_OK;


public class AuthModel {

    private static AuthModel INSTANCE;

    public static AuthModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AuthModel();
        }

        return INSTANCE;
    }

    public Observable<ApiResponse<User>> login(@NonNull final Authentication authentication) {
        return getService().login(authentication);
    }

    public Observable<ApiResponse<User>> register(@NonNull final Authentication authentication) {
        return MainApplication.isTestFramework() ? getMokcedRegister()
                : getService().register(authentication);
    }

    private Observable<ApiResponse<User>> getMokcedRegister() {
        final ApiResponse<User> apiResponse = new ApiResponse.Builder<User>()
                .withData(new User("Goku", "Vegeta",
                        new Authentication("saiyan@gmail.com", "1234")))
                .withStatus(STATUS_OK).build();

        return Observable.just(apiResponse);
    }

    private AuthService getService() {
        return MainApplication.getRetrofit().create(AuthService.class);
    }
}
