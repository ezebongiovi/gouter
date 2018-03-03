package com.testableapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;
import android.view.View;

import com.testableapp.activities.CreateEventActivity;
import com.testableapp.activities.LoginActivity;
import com.testableapp.base.BaseEspressoTest;
import com.testableapp.providers.AuthProvider;
import com.testableapp.providers.FacebookTestProvider;
import com.testableapp.widgets.PlacePicker;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasType;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Created by epasquale on 3/12/17.
 */

public class CreateEventTest extends BaseEspressoTest {

    @Override
    public void setUp() {
        super.setUp();
        Intents.init();

        Intents.intending(hasData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
                .respondWith(imageFromGallery());
    }

    @Test
    public void testCreateEvent() throws InterruptedException {
        login("nancy_gpcjads_mulligan@tfbnw.net", "qatest");

        onView(withId(R.id.button_create_event)).perform(click());

        onView(withId(R.id.searchField)).perform(click());

        onView(withId(R.id.searchField)).perform(typeText("Genova 2023"));

        Thread.sleep(PlacePicker.SEARCH_DEBOUNCE);

        onView(withId(R.id.placePickerList)).perform(RecyclerViewActions
                .actionOnItemAtPosition(0, click()));

        onView(withId(R.id.ms_stepNextButton)).perform(click());

        // Close keyboard
        pressBack();

        onView(withId(R.id.coverEmpty)).check(matches(isDisplayed()));

        onView(withId(R.id.coverEmpty)).perform(click());

        grantPermissionsIfNecessary();

        Intents.release();

        onView(withId(R.id.cover)).check(matches(isDisplayed()));

        onView(withId(R.id.coverEmpty)).check((matches(not(isDisplayed()))));

        onView(withId(R.id.descriptionField)).perform(typeText("Descripcion"));

        onView(withId(R.id.ms_stepNextButton)).perform(click());
    }

    @Override
    protected AuthProvider getProvider() {
        return new FacebookTestProvider();
    }

    private Instrumentation.ActivityResult imageFromGallery() {
        // Put the drawable in a bundle.
        final Uri imageUri = Uri.parse("kajsdkajsd");

        // Create the Intent that will include the bundle.
        final Intent resultData = new Intent();
        resultData.setData(imageUri);

        // Create the ActivityResult with the Intent.
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

    private void grantPermissionsIfNecessary() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final UiObject allowPermissions = UiDevice.getInstance(InstrumentationRegistry
                    .getInstrumentation()).findObject(new UiSelector().text("ALLOW"));

            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click();
                } catch (final UiObjectNotFoundException e) {
                    Log.i("CreateEventTest", "No permission dialog found");
                }
            }
        }
    }
}
