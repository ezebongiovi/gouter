package com.testableapp;

import com.testableapp.activities.LoginActivity;
import com.testableapp.presenters.LoginPresenter;
import com.testableapp.views.LoginView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AbstractMvpActivityTest {


    @InjectMocks
    final LoginActivity mActivity = new LoginActivity();

    @Mock
    LoginPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void testCreation() {
        // Verify view has never been attached
        verify(mPresenter, never()).attachView(mActivity);

        // Attach the view
        mActivity.onAttachedToWindow();

        // Verify the presenter has been notified about the view's attachment
        verify(mPresenter, Mockito.times(1)).attachView(any(LoginView.class));
    }

    @Test
    public void testDestruction() {
        // Verify view has never been detached
        verify(mPresenter, never()).detachView();

        // Detach the view
        mActivity.onDetachedFromWindow();

        // Verify the presenter has been notified about the view's detachment
        verify(mPresenter, Mockito.times(1)).detachView();
    }
}
