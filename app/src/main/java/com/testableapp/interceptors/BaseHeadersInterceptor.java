package com.testableapp.interceptors;

import android.content.Context;
import android.support.annotation.NonNull;

import com.testableapp.BuildConfig;
import com.testableapp.manager.AuthenticationManager;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BaseHeadersInterceptor implements Interceptor {
    private static final String HEADER_PLATFORM = "X-Platform";
    private static final String HEADER_VERSION = "X-Version";
    private static final String HEADER_API_KEY = "X-Api-Key";

    private final Context mContext;

    public BaseHeadersInterceptor(@NonNull final Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        final Headers.Builder headers = new Headers.Builder();

        if (AuthenticationManager.getInstance().getUser(mContext) != null) {
            headers.add("Authorization", "Bearer " + AuthenticationManager.getInstance()
                    .getUser(mContext).authentication.accessToken);
        }

        headers.add(HEADER_API_KEY, BuildConfig.API_KEY);
        headers.add(HEADER_PLATFORM, "Android");
        headers.add(HEADER_VERSION, BuildConfig.VERSION_NAME);

        final Request request = chain.request().newBuilder().headers(headers.build()).build();
        return chain.proceed(request);
    }
}
