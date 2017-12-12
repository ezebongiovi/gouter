package com.testableapp;

import android.support.test.espresso.contrib.RecyclerViewActions;

import com.testableapp.base.BaseEspressoTest;
import com.testableapp.providers.AuthProvider;
import com.testableapp.providers.FacebookTestProvider;
import com.testableapp.widgets.PlacePicker;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by epasquale on 3/12/17.
 */

public class CreateEventTest extends BaseEspressoTest {

    @Test
    public void testLogin() throws InterruptedException {
        login("nancy_gpcjads_mulligan@tfbnw.net", "qatest");

        onView(withId(R.id.button_create_event)).perform(click());

        onView(withId(R.id.searchField)).perform(typeText("Genova"));

        Thread.sleep(PlacePicker.SEARCH_DEBOUNCE);

        onView(withId(R.id.placePickerList)).check(matches(isDisplayed()));

        onView(withId(R.id.placePickerList)).perform(RecyclerViewActions
                .actionOnItemAtPosition(0, click()));
    }

    @Override
    protected AuthProvider getProvider() {
        return new FacebookTestProvider();
    }
}
