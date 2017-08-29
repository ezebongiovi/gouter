package com.testableapp;

import com.testableapp.dto.Authentication;
import com.testableapp.dto.User;
import com.testableapp.models.AuthModel;

import org.junit.Test;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;

public class AuthModelTest {
    private final AuthModel authModel = AuthModel.getInstance();

    @Test
    public void testLogin() throws Exception {
        final Authentication authentication = new Authentication("admin", "1234");
        final TestObserver<User> testObserver = TestObserver.create();
        authModel.login(authentication).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();

        final User user = (User) testObserver.getEvents().get(0).get(0);

        assertEquals(user.getAuthentication().getUserName(), authentication.getUserName());
        assertEquals(user.getAuthentication().getPassword(), authentication.getPassword());
    }
}
