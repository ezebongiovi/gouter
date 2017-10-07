package com.testableapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;

import com.testableapp.base.BaseEspressoTest;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class RegisterTest extends BaseEspressoTest {

    @Test
    public void testRegister() {
        try {
            onView(withId(R.id.noAccountButton)).check(matches(isDisplayed()));
        } catch (final NoMatchingViewException e) {
            logout();
        }

        onView(withId(R.id.noAccountButton)).perform(click());

        onView(withId(R.id.nameField)).perform(typeText("Ezequiel"));
        onView(withId(R.id.lastNameField)).perform(typeText("Di Pasquale"));
        onView(withId(R.id.userField)).perform(typeText("ezebongiovi@gmail.com"));
        onView(withId(R.id.passwordField)).perform(typeText("1234"));
        onView(withId(R.id.confirmPasswordField)).perform(typeText("4321"));

        onView(withId(R.id.registerButton)).perform(click());

        // Checks that register has failed
        onView(withId(R.id.registerButton)).check(matches(isDisplayed()));

        onView(withId(R.id.confirmPasswordField)).perform(replaceText("1234"));

        Espresso.pressBack();

        onView(withId(R.id.registerButton)).perform(click());

        logout();
    }
}
