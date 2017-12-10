package com.testableapp.models.mocks;

import android.support.annotation.Nullable;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.Authentication;
import com.testableapp.dto.Country;
import com.testableapp.dto.Image;
import com.testableapp.dto.User;
import com.testableapp.models.AuthModel;

import dalvik.annotation.TestTargetClass;
import io.reactivex.Observable;

import static com.testableapp.dto.ApiResponse.STATUS_ERROR;
import static com.testableapp.dto.ApiResponse.STATUS_OK;

/**
 * Created by epasquale on 3/12/17.
 */
public final class MockAuthModel {
    public static final Authentication MOCK_ERROR = new Authentication.Builder().withAccessToken("error")
            .withProviderName("error").build();

    public static Observable<ApiResponse<User>> login(@Nullable final Authentication authentication) {
        if (authentication != null && MOCK_ERROR.accessToken.equals(authentication.accessToken)
                && MOCK_ERROR.providerName.equals(authentication.providerName)) {

            return loginError();
        }

        final ApiResponse<User> apiResponse = new ApiResponse.Builder<User>()
                .withData(new User("286827", "Goku", "Vegeta",
                        new Image("http://ndl.mgccw.com/mu3/app/20140717/21/1405612487854/ss/4_small.png"),
                        "saiyan@gmail.com", new Authentication.Builder().withProviderName("Test")
                        .withAccessToken("a12y3871t2").build(), new Country("Argentina")))
                .withStatus(STATUS_OK).build();

        return Observable.just(apiResponse);
    }

    public static Observable<ApiResponse<User>> register() {
        return login(null);
    }

    private static Observable<ApiResponse<User>> loginError() {
        final ApiResponse<User> apiResponse = new ApiResponse.Builder<User>()
                .withStatus(STATUS_ERROR).withMessage("Datos inválidos").build();

        return Observable.just(apiResponse);
    }
}
