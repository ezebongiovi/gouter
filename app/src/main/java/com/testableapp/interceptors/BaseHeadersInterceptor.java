package com.testableapp.interceptors;

import com.testableapp.MainApplication;
import com.testableapp.manager.AuthenticationManager;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BaseHeadersInterceptor implements Interceptor {

    @Override
    public Response intercept(final Chain chain) throws IOException {
        final Headers.Builder headers = new Headers.Builder();

        if (AuthenticationManager.getInstance().getUser(MainApplication.getContext()) != null) {
            headers.add("Authorization", "Bearer " + AuthenticationManager.getInstance()
                    .getUser(MainApplication.getContext()).getAuthentication().getAccessToken());
        }

        final Request request = chain.request().newBuilder().headers(headers.build()).build();
        return chain.proceed(request);
    }
}
