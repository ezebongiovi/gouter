package com.testableapp.dto;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.testableapp.utils.JsonUtils;

import java.io.IOException;

import retrofit2.Response;

public class ApiResponse<T> {

    public static final String STATUS_OK = "OK";
    public static final String STATUS_ERROR = "ERROR";

    public final String message;
    public final String error;
    public final String status;
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
    public static ApiResponse httpError(final Response response) {
        final ApiResponse apiResponse = getApiError(response);
        return new ApiResponse(apiResponse.message, apiResponse.message, response != null
                ? String.valueOf(response.code()) : null, null, Kind.HTTP);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static ApiResponse networkError(final IOException exception) {
        return new ApiResponse(exception.getMessage(), null, null, null, Kind.NETWORK);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static ApiResponse unexpectedError(final Throwable exception) {
        return new ApiResponse(exception.getMessage(), null, null, null, Kind.UNEXPECTED);
    }

    @SuppressWarnings("NullableProblems")
    private ApiResponse(final String message, @Nullable final String error, @Nullable final String status,
                        @NonNull final T data, final Kind kind) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.kind = kind;
        this.data = data;
    }

    private ApiResponse(final Builder<T> builder) {
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

        public Builder withKind(final Kind kind) {
            this.kind = kind;
            return this;
        }

        public ApiResponse build() {
            return new ApiResponse(this);
        }
    }

    @Override
    public String toString() {
        return "ApiResponse{"
                + "message='" + message + '\''
                + ", error='" + error + '\''
                + ", status=" + status
                + ", kind=" + kind
                + '}';
    }

    public static ApiResponse getApiError(final Response response) {
        ApiResponse apiResponse = null;
        try {
            apiResponse = getErrorBodyAs(ApiResponse.class, response);
        } catch (final Exception exc) {
            // TODO: Log error
        }
        return (apiResponse != null) ? apiResponse : new ApiResponse.Builder().withKind(Kind.UNEXPECTED).build();
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
