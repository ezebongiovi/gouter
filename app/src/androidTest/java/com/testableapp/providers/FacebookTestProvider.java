package com.testableapp.providers;

import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.testableapp.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by epasquale on 3/12/17.
 */

public class FacebookTestProvider implements AuthProvider {
    private static final int TIME_OUT = 10000;

    @Override
    public void login(@NonNull final String email, @NonNull final String password) {
        final UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        try {
            onView(withId(R.id.button_login_facebook)).check(matches(isDisplayed()))
                    .perform(click());

            uiDevice.wait(Until.findObject(By.clazz(WebView.class)), TIME_OUT);

            // Set Login
            final UiObject emailInput = uiDevice.findObject(new UiSelector()
                    .instance(0)
                    .className(EditText.class));

            emailInput.waitForExists(TIME_OUT);
            emailInput.setText(email);

            // Set Password
            final UiObject passwordInput = uiDevice.findObject(new UiSelector()
                    .instance(1)
                    .className(EditText.class));

            passwordInput.waitForExists(TIME_OUT);
            passwordInput.setText(password);

            // Confirm Button Click
            final UiObject buttonLogin = uiDevice.findObject(new UiSelector()
                    .instance(0)
                    .className(Button.class));

            buttonLogin.waitForExists(TIME_OUT);
            buttonLogin.clickAndWaitForNewWindow();

            facebookLoginStep2(uiDevice);

        } catch (final UiObjectNotFoundException e) {
            if ("setText".equals(e.getStackTrace()[0].getMethodName())) {
                try {
                    facebookLoginStep2(uiDevice);
                } catch (final UiObjectNotFoundException e1) {
                    logout();

                    // User's already logged in
                    login(email, password);
                }
            }
        }
    }

    @Override
    public void logout() {
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.buttonLogout)).perform(click());
    }

    private void facebookLoginStep2(final UiDevice uiDevice) throws UiObjectNotFoundException {
        // Facebook WebView - Page 2
        final UiObject buttonOk = uiDevice.findObject(new UiSelector()
                .instance(0)
                .className(Button.class));

        buttonOk.waitForExists(TIME_OUT);
        buttonOk.clickAndWaitForNewWindow();
    }
}
