package com.testableapp.manager;

import android.content.Context;
import android.support.annotation.NonNull;

import com.testableapp.db.DBHelper;
import com.testableapp.dto.User;

public class AuthenticationManager {

    private static final AuthenticationManager INSTANCE = new AuthenticationManager();
    private User mUser;

    public static AuthenticationManager getInstance() {
        return INSTANCE;
    }

    public void onLogin(@NonNull final Context context, @NonNull final User user) {
        mUser = user;
        DBHelper.getInstance(context).onLogin(user);
    }

    public User getUser(@NonNull final Context context) {
        if (mUser != null) {
            return mUser;
        }

        return DBHelper.getInstance(context).getLoggedUser();
    }

    public void logOut(@NonNull final Context context) {
        mUser = null;
        DBHelper.getInstance(context).onLogOut();
    }
}
