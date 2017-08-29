package com.testableapp.dto;

import android.support.annotation.Nullable;

import com.testableapp.utils.JsonUtils;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class ApiError {

    public final String message;
    public final String error;
    public final Integer status;
    public final List<Cause> cause;
    public final Kind kind;

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

    public static ApiError httpError(final Response response) {
        final ApiError apiError = getApiError(response);
        return new ApiError(apiError.message, apiError.message, response != null ? response.code() : null,
                apiError.cause, Kind.HTTP);
    }

    public static ApiError networkError(final IOException exception) {
        return new ApiError(exception.getMessage(), null, null, null, Kind.NETWORK);
    }

    public static ApiError unexpectedError(final Throwable exception) {
        return new ApiError(exception.getMessage(), null, null, null, Kind.UNEXPECTED);
    }

    public ApiError(final String message, @Nullable final String error, @Nullable final Integer status,
                    @Nullable final List cause, final Kind kind) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.cause = cause;
        this.kind = kind;
    }

    private ApiError(final Builder builder) {
        message = builder.message;
        error = builder.error;
        status = builder.status;
        cause = builder.cause;
        kind = builder.kind;
    }

    public static class Builder {
        private String message;
        private String error;
        private Integer status;
        private List<Cause> cause;
        private Kind kind;

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

        public Builder withParams(List<Cause> cause){
            this.cause = cause;
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

    public int getErrorCode() {
        return (cause != null && !cause.isEmpty()) ? cause.get(0).getCode() : 0;
    }

    public class Cause {
        private final String code;

        public Cause(final String code) {
            this.code = code;
        }

        public int getCode() {
            return Integer.valueOf(code);
        }
    }

    @Override
    public String toString() {
        return "ApiError{"
                + "message='" + message + '\''
                + ", error='" + error + '\''
                + ", status=" + status
                + ", cause=" + cause
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
