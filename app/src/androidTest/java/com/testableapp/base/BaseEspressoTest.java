package com.testableapp.base;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jakewharton.espresso.OkHttp3IdlingResource;
import com.testableapp.MainApplication;
import com.testableapp.R;
import com.testableapp.activities.LoginActivity;
import com.testableapp.providers.AuthProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import io.reactivex.annotations.NonNull;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public abstract class BaseEspressoTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    private IdlingResource httpResource;

    @Before
    public void setUp() {
        MainApplication.initTestFramework();
        httpResource = OkHttp3IdlingResource.create("OkHttp", MainApplication
                .getClient(InstrumentationRegistry.getContext()));
        registerIdlingResources(httpResource);
    }

    protected void login(@NonNull final String email, @NonNull final String password) {
        final AuthProvider provider = getProvider();

        provider.login(email, password);
    }

    protected abstract AuthProvider getProvider();

    @After
    public void tearDown() {
        unregisterIdlingResources(httpResource);
    }
}
