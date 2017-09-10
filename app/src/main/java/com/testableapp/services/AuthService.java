package com.testableapp.services;

import android.support.annotation.NonNull;

import com.testableapp.dto.Authentication;
import com.testableapp.dto.User;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("auth")
    Observable<User> login(@Body @NonNull final Authentication authentication);

    @POST("signup")
    Observable<User> register(Authentication authentication);
}
