package com.testableapp.rx;


import android.support.annotation.NonNull;

import com.testableapp.dto.ApiError;

import java.io.IOException;

import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_GATEWAY_TIMEOUT;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

public abstract class ErrorConsumer implements Consumer<Throwable> {

    @Override
    public void accept(final Throwable t) throws Exception {
        if (t instanceof HttpException) {

            final HttpException httpException = (HttpException) t;

            if (httpException.code() == HTTP_UNAUTHORIZED) {
                // TODO: Handle case
            } else if (httpException.code() >= HTTP_BAD_REQUEST && httpException.code()
                    < HTTP_INTERNAL_ERROR) {
                onError(ApiError.httpError(httpException.response()));
            } else if (httpException.code() >= HTTP_INTERNAL_ERROR && httpException.code()
                    <= HTTP_GATEWAY_TIMEOUT) {
                onError(ApiError.httpError(httpException.response()));
            } else {
                onError(ApiError.unexpectedError(t));
            }
        } else if (t instanceof IOException) {
            onError(ApiError.networkError((IOException) t));
        } else {
            onError(ApiError.unexpectedError(t));
        }
    }

    public abstract void onError(@NonNull ApiError apiError);
}
