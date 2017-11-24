package com.testableapp.interceptors;

import android.content.Context;
import android.support.annotation.NonNull;

import com.testableapp.dto.Authentication;
import com.testableapp.manager.AuthenticationManager;

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

        final String accessToken = response.header("x-token");

        if (accessToken != null) {
            final Authentication auth = new Authentication.Builder().withAccessToken(accessToken)
                    .withProviderName(AuthenticationManager.getInstance().getUser(mContext)
                            .authentication.providerName).build();

            AuthenticationManager.getInstance().updateAuthentication(mContext, auth);
        }

        final ResponseBody body = response.body();

        return response.newBuilder()
                .body(ResponseBody.create(body.contentType(), body.contentLength(),
                        body.source())).build();
    }
}
