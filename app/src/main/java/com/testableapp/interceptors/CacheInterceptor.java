package com.testableapp.interceptors;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {

    private final Context mContext;

    public CacheInterceptor(@NonNull final Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(@NonNull final Interceptor.Chain chain) throws IOException {

        final NetworkInfo info = ((ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        Request request = chain.request();

        if (info == null) {
            request = request.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Access-Control-Allow-Origin")
                    .removeHeader("Vary")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control",
                            "public, only-if-cached, max-stale= 60")
                    .build();
        }

        return chain.proceed(request);
    }
}