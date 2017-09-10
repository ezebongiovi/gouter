package com.testableapp.dto;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.testableapp.utils.JsonUtils;

import java.io.IOException;

import retrofit2.Response;

public class ApiError<T> {

    public final String message;
    public final String error;
    public final Integer status;
    public final Kind kind;
    public final T data;

    public enum Kind {
        /**
         * An {@link IOException} occurred while communicating to the server.
         */
        NETWORK,
        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static ApiError httpError(final Response response) {
        final ApiError apiError = getApiError(response);
        return new ApiError(apiError.message, apiError.message, response != null
                ? response.code(): null, null, Kind.HTTP);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static ApiError networkError(final IOException exception) {
        return new ApiError(exception.getMessage(), null, null, null, Kind.NETWORK);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static ApiError unexpectedError(final Throwable exception) {
        return new ApiError(exception.getMessage(), null, null, null, Kind.UNEXPECTED);
    }

    @SuppressWarnings("NullableProblems")
    private ApiError(final String message, @Nullable final String error, @Nullable final Integer status,
                     @NonNull final T data, final Kind kind) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.kind = kind;
        this.data = data;
    }

    private ApiError(final Builder<T> builder) {
        message = builder.message;
        error = builder.error;
        status = builder.status;
        kind = builder.kind;
        data = builder.data;
    }

    public static class Builder<T> {
        private String message;
        private String error;
        private Integer status;
        private Kind kind;
        private T data;

        public Builder withMessage(final String message) {
            this.message = message;
            return this;
        }

        public Builder withError(final String error) {
            this.error = error;
            return this;
        }

        public Builder withStatus(Integer status) {
            this.status = status;
            return this;
        }

        public Builder withData(@NonNull final T data) {
            this.data = data;
            return this;
        }

        public Builder withKind(final Kind kind) {
            this.kind = kind;
            return this;
        }

        public ApiError build() {
            return new ApiError(this);
        }
    }

    @Override
    public String toString() {
        return "ApiError{"
                + "message='" + message + '\''
                + ", error='" + error + '\''
                + ", status=" + status
                + ", kind=" + kind
                + '}';
    }

    public static ApiError getApiError(final Response response) {
        ApiError apiError = null;
        try {
            apiError = getErrorBodyAs(ApiError.class, response);
        } catch (final Exception exc) {
            // TODO: Log error
        }
        return (apiError != null) ? apiError : new ApiError.Builder().withKind(Kind.UNEXPECTED).build();
    }

    /**
     * HTTP response body converted to specified {@code type}. {@code null} if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified {@code type}.
     */
    @Nullable
    public static <T> T getErrorBodyAs(final Class<T> type, final Response response) throws IOException {
        if (response == null || response.errorBody() == null) {
            return null;
        }
        return JsonUtils.getInstance().fromJson(response.errorBody().string(), type);
    }
}
