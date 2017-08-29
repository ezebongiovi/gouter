package com.testableapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import com.testableapp.R;
import com.testableapp.dto.User;
import com.testableapp.presenters.LoginPresenter;
import com.testableapp.views.LoginView;

public class LoginActivity extends AbstractActivity<LoginPresenter> implements LoginView {

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                          @NonNull final LoginPresenter presenter) {

        final EditText mUserField = (EditText) findViewById(R.id.userField);
        final EditText mPasswordField = (EditText) findViewById(R.id.passwordField);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                presenter.login(mUserField.getText().toString(),
                        mPasswordField.getText().toString());
            }
        });
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @NonNull
    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onLogin(@NonNull final User user) {
        startActivity(ProfileActivity.getIntent(LoginActivity.this, user));
    }

    @Override
    public void onError() {
        Snackbar.make(findViewById(R.id.rootView), "Ups algo salió mal", Snackbar.LENGTH_LONG).show();
    }
}
