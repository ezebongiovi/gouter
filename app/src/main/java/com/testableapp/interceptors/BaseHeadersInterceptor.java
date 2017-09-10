package com.testableapp.interceptors;

import android.content.Context;
import android.support.annotation.NonNull;

import com.testableapp.manager.AuthenticationManager;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BaseHeadersInterceptor implements Interceptor {

    private final Context mContext;

    public BaseHeadersInterceptor(@NonNull final Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        final Headers.Builder headers = new Headers.Builder();

        if (AuthenticationManager.getInstance().getUser(mContext) != null) {
            headers.add("X-token", AuthenticationManager.getInstance().getUser(mContext)
                    .getAuthentication().getToken());
        }

        final Request request = chain.request().newBuilder().headers(headers.build()).build();
        return chain.proceed(request);
    }
}
