package com.testableapp;

import com.testableapp.base.BaseEspressoTest;

import org.junit.Test;

public class LoginTest extends BaseEspressoTest {

    @Test
    public void testEntry() {
        login("admin", "1234");
    }
}
