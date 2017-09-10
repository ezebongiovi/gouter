package com.testableapp;

import com.testableapp.presenters.RegisterPresenter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.mockito.MockitoAnnotations.initMocks;

public class RegisterActivityTest {
    @InjectMocks
    private RegisterPresenter registerPresenter;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testValidation() {
        Assert.assertTrue(registerPresenter.isValidEntry("ezebongiovi@gmail.com",
                "1234", "1234"));

        Assert.assertFalse(registerPresenter.isValidEntry("ezebongiovi@gmail.com",
                "1234", "4321"));
    }
}
