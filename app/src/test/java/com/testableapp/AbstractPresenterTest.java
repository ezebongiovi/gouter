package com.testableapp;

import com.testableapp.activities.LoginActivity;
import com.testableapp.base.BaseJUnitTest;
import com.testableapp.dto.Authentication;
import com.testableapp.presenters.LoginPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class AbstractPresenterTest extends BaseJUnitTest {

    @InjectMocks
    LoginPresenter loginPresenter;

    @Mock
    final LoginActivity loginActivity = new LoginActivity();

    @Before
    public void setUp() {
        super.setUp();
        initMocks(this);
    }

    @Test
    public void testAttachment() {
        loginPresenter.attachView(loginActivity);

        assertEquals(0, loginPresenter.getCompositeDisposable().size());

        loginPresenter.login(new Authentication.Builder().withAccessToken("testing")
                .withProviderName("facebook").build());

        assertEquals(1, loginPresenter.getCompositeDisposable().size());
    }

    @After
    public void testDestroy() {
        loginPresenter.detachView();

        assertEquals(0, loginPresenter.getCompositeDisposable().size());
    }
}
