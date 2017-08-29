package com.testableapp;

import com.testableapp.models.AuthModel;
import com.testableapp.presenters.LoginPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.mockito.MockitoAnnotations.initMocks;

public class LoginPresenterTest {

    @InjectMocks
    private LoginPresenter loginPresenter;
    private AuthModel authModel = AuthModel.getInstance();

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testSuccess() {
        loginPresenter.login("admin", "1234");

        while (loginPresenter.getCompositeDisposable().size() > 0) {

        }
    }
}
