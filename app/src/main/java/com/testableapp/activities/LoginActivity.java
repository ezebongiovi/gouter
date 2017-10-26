package com.testableapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.testableapp.R;
import com.testableapp.dto.Authentication;
import com.testableapp.dto.User;
import com.testableapp.manager.AuthenticationManager;
import com.testableapp.presenters.LoginPresenter;
import com.testableapp.views.LoginView;

public class LoginActivity extends AbstractMvpActivity<LoginPresenter> implements LoginView {

    private CallbackManager mCallbackManager;

    @Override
    protected boolean shouldAuthenticate() {
        return false;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                 @NonNull final LoginPresenter presenter) {

        if (AuthenticationManager.getInstance().getUser(this) != null) {
            startActivity(new Intent(this, NavigationActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
            return;
        }

        mCallbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(getResources().getStringArray(R.array.facebook_permissions));
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                presenter.login(new Authentication.Builder()
                        .withAccessToken(loginResult.getAccessToken().getToken())
                        .withProviderName(Authentication.PROVIDER_FACEBOOK).build());
            }

            @Override
            public void onCancel() {
                // Nothing to do
            }

            @Override
            public void onError(final FacebookException exception) {
                // TODO: Track error event
            }
        });
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @NonNull
    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onLogin(@NonNull final User user) {
        AuthenticationManager.getInstance().onLogin(this, user);
        startActivity(new Intent(this, NavigationActivity.class));
    }
}
