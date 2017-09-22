package com.testableapp.services;

import android.support.annotation.NonNull;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsersService {

    @GET("/users")
    Observable<ApiResponse<List<User>>> searchPeople(@Query("name")@NonNull String criteria);
}
