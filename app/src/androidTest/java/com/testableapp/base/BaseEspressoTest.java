package com.testableapp.base;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jakewharton.espresso.OkHttp3IdlingResource;
import com.squareup.rx2.idler.Rx2Idler;
import com.testableapp.MainApplication;
import com.testableapp.activities.LoginActivity;
import com.testableapp.dto.User;
import com.testableapp.manager.AuthenticationManager;
import com.testableapp.providers.AuthProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import io.reactivex.annotations.NonNull;
import io.reactivex.plugins.RxJavaPlugins;

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

        RxJavaPlugins.setInitIoSchedulerHandler(Rx2Idler.create("RxJava"));

        IdlingRegistry.getInstance().register(httpResource);
    }

    protected void login(@NonNull final String email, @NonNull final String password) {

        /**
         * If user's not logged in we call provider for login
         */
        final User user = AuthenticationManager.getInstance()
                .getUser(InstrumentationRegistry.getContext());

        if (user == null || !email.equals(user.email)) {
            final AuthProvider provider = getProvider();

            provider.login(email, password);
        }
    }

    protected abstract AuthProvider getProvider();

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(httpResource);
    }
}
