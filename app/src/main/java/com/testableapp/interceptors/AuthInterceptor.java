package com.testableapp.interceptors;

import android.content.Context;
import android.support.annotation.NonNull;

import com.testableapp.dto.ApiResponse;
import com.testableapp.manager.AuthenticationManager;
import com.testableapp.utils.JsonUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AuthInterceptor implements Interceptor {

    private final Context mContext;

    /**
     * Intercepts API responses updating the authentication if it's retrieved by the server.
     *
     * @param context the application's context
     */
    public AuthInterceptor(@NonNull final Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        final Response response = chain.proceed(chain.request());

        final String JSON = response.body().string();
        final ApiResponse apiResponse = JsonUtils.getInstance().fromJson(JSON, ApiResponse.class);

        if (apiResponse != null && apiResponse.authentication != null) {
            // Updates user authentication
            AuthenticationManager.getInstance().updateAuthentication(mContext,
                    apiResponse.authentication);
        }

        return response.newBuilder().body(ResponseBody
                .create(response.body().contentType(), JSON)).build();
    }
}
