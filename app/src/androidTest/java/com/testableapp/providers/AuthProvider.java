package com.testableapp.providers;

import io.reactivex.annotations.NonNull;

/**
 * Created by epasquale on 3/12/17.
 */

public interface AuthProvider {
    void login(@NonNull String email, @NonNull String password);

    void logout();
}
