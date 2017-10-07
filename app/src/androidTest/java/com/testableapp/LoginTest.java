package com.testableapp;

import android.support.test.espresso.NoMatchingViewException;

import com.testableapp.base.BaseEspressoTest;
import com.testableapp.models.AuthModel;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class LoginTest extends BaseEspressoTest {

    @Test
    public void testLogin() {
        login(AuthModel.MOCK_ERROR.getEmail(), AuthModel.MOCK_ERROR.getPassword());

        login("auth", "auth");

        logout();
    }

    @Test
    public void testRegister() {
        try {
            logout();
        } catch (NoMatchingViewException e) {
            // It means we're not logged in
        }

        onView(withId(R.id.noAccountButton)).perform(click());
        onView(withId(R.id.nameField)).check(matches(isDisplayed()));
    }
}
