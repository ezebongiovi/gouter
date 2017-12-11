package com.testableapp.providers;

import com.facebook.login.LoginManager;

public class FacebookProvider extends AbstractProvider {

    @Override
    public void logout() {
        LoginManager.getInstance().logOut();
    }
}
