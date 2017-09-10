package com.testableapp.base;

import android.support.annotation.NonNull;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jakewharton.espresso.OkHttp3IdlingResource;
import com.testableapp.MainApplication;
import com.testableapp.R;
import com.testableapp.activities.LoginActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public abstract class BaseEspressoTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    private IdlingResource httpResource;

    @Before
    public void setUp() {
        httpResource = OkHttp3IdlingResource.create("OkHttp", MainApplication
                .getClient());
        registerIdlingResources(httpResource);
    }

    @After
    public void tearDown() {
        unregisterIdlingResources(httpResource);
    }

    protected void login(@NonNull final String email, @NonNull final String password) {
        try {
            onView(withId(R.id.userField)).check(matches(isDisplayed()));

            onView(withId(R.id.userField)).perform(typeText(email));
            onView(withId(R.id.passwordField)).perform(typeText(password));

            onView(withId(R.id.loginButton)).perform(click());
        } catch (Exception e) {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

            onView(withText(R.string.action_logout)).perform(click());

            // User's already logged in
            login(email, password);
        }
    }
}
