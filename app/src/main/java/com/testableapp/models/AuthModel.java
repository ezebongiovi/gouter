package com.testableapp.models;

import android.support.annotation.NonNull;

import com.testableapp.MainApplication;
import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.Authentication;
import com.testableapp.dto.RegistrationRequest;
import com.testableapp.dto.User;
import com.testableapp.models.mocks.MockAuthModel;
import com.testableapp.services.AuthService;

import io.reactivex.Observable;


public class AuthModel {

    private static AuthModel INSTANCE;

    public static AuthModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AuthModel();
        }

        return INSTANCE;
    }

    public Observable<ApiResponse<User>> login(@NonNull final Authentication authentication) {
        return MainApplication.isTestFramework() ? MockAuthModel.login(authentication)
                : getService().login(authentication);
    }

    private AuthService getService() {
        return MainApplication.getRetrofit().create(AuthService.class);
    }
}
