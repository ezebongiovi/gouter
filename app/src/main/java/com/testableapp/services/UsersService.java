package com.testableapp.services;

import android.support.annotation.NonNull;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.Search;
import com.testableapp.dto.User;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsersService {

    @GET("/users")
    Observable<ApiResponse<Search<User>>> searchPeople(@Query("name") @NonNull String criteria,
                                                       @Query("offset") int offset,
                                                       @Query("limit") int limit);
}
