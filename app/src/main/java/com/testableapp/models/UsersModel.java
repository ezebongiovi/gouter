package com.testableapp.models;

import com.testableapp.MainApplication;
import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.User;
import com.testableapp.services.UsersService;

import java.util.List;

import io.reactivex.Observable;

public class UsersModel {

    private static UsersModel INSTANCE;

    private UsersModel() {

    }

    public static UsersModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UsersModel();
        }

        return INSTANCE;
    }

    public Observable<ApiResponse<List<User>>> searchPeople(final String criteria) {
        return getService().searchPeople(criteria);
    }

    private UsersService getService() {
        return MainApplication.getRetrofit().create(UsersService.class);
    }
}
