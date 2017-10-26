package com.testableapp.manager;

import android.content.Context;
import android.support.annotation.NonNull;

import com.testableapp.MainApplication;
import com.testableapp.db.DBHelper;
import com.testableapp.dto.Authentication;
import com.testableapp.dto.Country;
import com.testableapp.dto.User;

import java.io.IOException;

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
        try {
            MainApplication.getClient(context).cache().delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DBHelper.getInstance(context).onLogOut();
    }

    public void updateAuthentication(@NonNull final Context context,
                                     @NonNull final Authentication authentication) {
        DBHelper.getInstance(context).onLogin(new User(mUser._id, mUser.firstName, mUser.lastName,
                mUser.profilePicture, authentication, mUser.country));

        mUser = getUser(context);
    }

    public boolean isAuthenticated() {
        return mUser != null;
    }
}
