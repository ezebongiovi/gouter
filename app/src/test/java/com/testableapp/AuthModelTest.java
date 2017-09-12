package com.testableapp;

import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.Authentication;
import com.testableapp.dto.User;
import com.testableapp.models.AuthModel;

import org.junit.Test;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AuthModelTest {
    private final AuthModel authModel = AuthModel.getInstance();

    @Test
    public void testLogin() throws Exception {
        final Authentication authentication = new Authentication("ezebongiovi@gmail.com", "1234");
        final TestObserver<ApiResponse<User>> testObserver = TestObserver.create();
        authModel.login(authentication).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();

        final ApiResponse<User> response = (ApiResponse<User>) testObserver
                .getEvents().get(0).get(0);

        assertEquals(response.data.getAuthentication().getEmail(), authentication.getEmail());
        assertTrue(!response.data.getAuthentication().getAccessToken().isEmpty());
        assertEquals(ApiResponse.STATUS_OK, response.status);
    }
}
