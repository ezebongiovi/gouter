package com.testableapp.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.testableapp.MainApplication;
import com.testableapp.utils.SessionUtils;
import com.testableapp.dto.Authentication;
import com.testableapp.dto.User;
import com.testableapp.providers.AbstractProvider;
import com.testableapp.utils.ProviderUtils;

import java.io.IOException;

public class AuthenticationManager {

    private static final AuthenticationManager INSTANCE = new AuthenticationManager();
    private User mUser;
    private AbstractProvider mProvider;

    public static AuthenticationManager getInstance() {
        return INSTANCE;
    }

    public void onLogin(@NonNull final Context context, @NonNull final User user) {
        mUser = user;
        mProvider = ProviderUtils.getProvider(user.authentication.providerName);

        SessionUtils.onLogin(context, user);
    }

    public User getUser(@NonNull final Context context) {
        if (mUser != null) {
            return mUser;
        }

        return SessionUtils.getLoggedUser(context);
    }

    @Nullable
    private AbstractProvider getProvider(@NonNull final Context context) {
        final User user = mUser == null ? getUser(context) : mUser;

        if (user != null) {
            return ProviderUtils.getProvider(user.authentication.providerName);
        }

        return null;
    }

    public void logOut(@NonNull final Context context) {
        mUser = null;

        try {
            MainApplication.getClient(context).cache().delete();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        getProvider(context).logout();
        SessionUtils.onLogOut(context);
    }

    public void updateAuthentication(@NonNull final Context context,
                                     @NonNull final Authentication authentication) {
        SessionUtils.onLogin(context, new User(mUser._id, mUser.firstName, mUser.lastName,
                mUser.profilePicture, mUser.email, authentication, mUser.country));

        mUser = getUser(context);
    }

    public boolean isAuthenticated(@NonNull final Context context) {
        return getUser(context) != null || (mProvider != null && mProvider.isLoggedIn());
    }
}
