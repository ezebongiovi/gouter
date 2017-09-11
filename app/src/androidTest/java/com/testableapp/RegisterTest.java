package com.testableapp;

import android.support.test.espresso.NoMatchingViewException;

import com.testableapp.base.BaseEspressoTest;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class RegisterTest extends BaseEspressoTest {

    @Test
    public void testRegister() {
        try {
            onView(withId(R.id.noAccountButton)).check(matches(isDisplayed()));
        } catch (final NoMatchingViewException e) {
            logout();
        }

        onView(withId(R.id.noAccountButton)).perform(scrollTo(), click());

        onView(withId(R.id.userField)).perform(scrollTo(), typeText("ezebongiovi@gmail.com"));
        onView(withId(R.id.passwordField)).perform(scrollTo(), typeText("1234"));
        onView(withId(R.id.confirmPasswordField)).perform(scrollTo(), typeText("423"));

        onView(withId(R.id.registerButton)).perform(scrollTo(), click());

        // Checks that register has failed
        onView(withId(R.id.registerButton)).check(matches(isDisplayed()));

        onView(withId(R.id.confirmPasswordField)).perform(replaceText("1234"));

        onView(withId(R.id.registerButton)).perform(scrollTo(), click());

        onView(withId(R.id.action_settings)).perform(click());

        onView(withText("ezebongiovi@gmail.com")).check(matches(isDisplayed()));
    }
}
