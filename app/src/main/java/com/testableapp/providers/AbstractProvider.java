package com.testableapp.providers;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class AbstractProvider {

    public static final String PROVIDER_FACEBOOK = "facebook";

    @StringDef(PROVIDER_FACEBOOK)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Proviers {}

    public abstract void logout();

    public abstract boolean isLoggedIn();

    public abstract String getToken();
}
