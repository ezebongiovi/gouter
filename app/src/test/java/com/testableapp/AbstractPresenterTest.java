package com.testableapp;

import com.testableapp.activities.LoginActivity;
import com.testableapp.presenters.LoginPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static junit.framework.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class AbstractPresenterTest {

    @InjectMocks
    LoginPresenter loginPresenter;

    @InjectMocks
    final LoginActivity loginActivity = new LoginActivity();

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testAttachment() {
        loginPresenter.attachView(loginActivity);

        assertEquals(0, loginPresenter.getCompositeDisposable().size());

        loginPresenter.login("admin", "1234");

        assertEquals(1, loginPresenter.getCompositeDisposable().size());
    }

    @After
    public void testDestroy() {
        loginPresenter.detachView();

        assertEquals(0, loginPresenter.getCompositeDisposable().size());
    }
}
