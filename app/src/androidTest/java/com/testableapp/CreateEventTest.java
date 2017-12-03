package com.testableapp;

import com.testableapp.base.BaseEspressoTest;
import com.testableapp.providers.AuthProvider;
import com.testableapp.providers.FacebookTestProvider;

import org.junit.Test;

/**
 * Created by epasquale on 3/12/17.
 */

public class CreateEventTest extends BaseEspressoTest {

    @Test
    public void testLogin() {
        login("nancy_gpcjads_mulligan@tfbnw.net", "qatest");

        // TODO: Finish create vents test
    }

    @Override
    protected AuthProvider getProvider() {
        return new FacebookTestProvider();
    }
}
