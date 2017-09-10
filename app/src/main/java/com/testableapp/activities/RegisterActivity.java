package com.testableapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import com.testableapp.R;
import com.testableapp.dto.User;
import com.testableapp.manager.AuthenticationManager;
import com.testableapp.presenters.RegisterPresenter;
import com.testableapp.views.RegisterView;

public class RegisterActivity extends AbstractMvpActivity<RegisterPresenter>
        implements RegisterView {

    @Override
    protected boolean shouldAuthenticate() {
        return false;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                 @NonNull final RegisterPresenter presenter) {
        final EditText emailView = (EditText) findViewById(R.id.userField);
        final EditText passwordView = (EditText) findViewById(R.id.passwordField);
        final EditText confirmView = (EditText) findViewById(R.id.confirmPasswordField);


        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                presenter.register(emailView.getText().toString(), passwordView.getText().toString(),
                        confirmView.getText().toString());
            }
        });
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_register;
    }

    @NonNull
    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public void onInvalidData() {
        Snackbar.make(findViewById(R.id.rootView), "Los datos son inválidos", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onRegister(@NonNull final User user) {
        AuthenticationManager.getInstance().onLogin(this, user);
        startActivity(ProfileActivity.getIntent(this, user)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
