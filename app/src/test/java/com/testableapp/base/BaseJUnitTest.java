package com.testableapp.base;

import android.support.annotation.CallSuper;

import com.testableapp.MainApplication;

import org.junit.Before;

public class BaseJUnitTest {

    @Before
    @CallSuper
    public void setUp() {
        MainApplication.initTestFramework();
    }
}
