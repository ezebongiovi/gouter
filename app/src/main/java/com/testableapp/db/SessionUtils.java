package com.testableapp.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.testableapp.dto.User;

public class SessionUtils {
    private static final String ACCOUNT_USER = "account-user";
    private static final Gson gson = new GsonBuilder().create();

    private SessionUtils() {
        throw new AssertionError("");
    }

    /**
     * Called on login for saving user's data on preferences
     *
     * @param context the application's context
     * @param user the user being saved
     */
    public static void onLogin(@NonNull final Context context, @NonNull final User user) {
        final SharedPreferences preferences = context.getSharedPreferences(Context.ACCOUNT_SERVICE,
                Context.MODE_PRIVATE);
        preferences.edit().putString(ACCOUNT_USER, gson.toJson(user)).apply();
    }

    /**
     * Gets the logged user from preferences
     *
     * @param context the application's context
     * @return the user's data from preferences. If there's no data returns null.
     */
    @Nullable
    public static User getLoggedUser(@NonNull final Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(Context.ACCOUNT_SERVICE,
                Context.MODE_PRIVATE);

        if (preferences.contains(ACCOUNT_USER)) {
            return gson.fromJson(preferences.getString(ACCOUNT_USER, null), User.class);
        }

        return null;
    }

    /**
     * Called from clearing data from preferences
     *
     * @param context the application's context
     */
    public static void onLogOut(@NonNull final Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(Context.ACCOUNT_SERVICE,
                Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }
}