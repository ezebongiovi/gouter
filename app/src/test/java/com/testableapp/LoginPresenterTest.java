package com.testableapp;

import com.testableapp.base.BaseJUnitTest;
import com.testableapp.presenters.LoginPresenter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.mockito.MockitoAnnotations.initMocks;

public class LoginPresenterTest extends BaseJUnitTest {

    @InjectMocks
    private LoginPresenter loginPresenter;

    @Before
    public void setUp() {
        super.setUp();
        initMocks(this);
    }

    @Test
    public void testSuccess() {
        loginPresenter.login("admin", "1234");

        Assert.assertTrue(loginPresenter.getCompositeDisposable().size() > 0);

        loginPresenter.detachView();

        Assert.assertTrue(loginPresenter.getCompositeDisposable().size() == 0);
    }
}
