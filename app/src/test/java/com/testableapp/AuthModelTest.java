package com.testableapp;

import com.testableapp.base.BaseJUnitTest;
import com.testableapp.dto.ApiResponse;
import com.testableapp.dto.Authentication;
import com.testableapp.dto.User;
import com.testableapp.models.AuthModel;
import com.testableapp.models.mocks.MockAuthModel;

import org.junit.Test;

import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AuthModelTest extends BaseJUnitTest {
    private final AuthModel authModel = AuthModel.getInstance();

    @Test
    public void testLoginSuccess() throws Exception {
        final Authentication authentication = new Authentication.Builder().withAccessToken("test")
                .withProviderName("facebook").build();
        final TestObserver<ApiResponse<User>> testObserver = TestObserver.create();
        authModel.login(authentication).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();

        final ApiResponse<User> response = (ApiResponse<User>) testObserver
                .getEvents().get(0).get(0);

        assertNotNull(response.data.authentication);
        assertTrue(!response.data.authentication.accessToken.isEmpty());
        assertEquals(ApiResponse.STATUS_OK, response.status);
    }

    @Test
    public void testLoginError() throws Exception {
        final Authentication auth = MockAuthModel.MOCK_ERROR;
        final TestObserver<ApiResponse<User>> testObserver = TestObserver.create();

        authModel.login(auth).subscribe(testObserver);

        testObserver.assertNoErrors();

        final ApiResponse<User> response = (ApiResponse<User>) testObserver.getEvents()
                .get(0).get(0);

        assertTrue(ApiResponse.STATUS_ERROR.equals(response.status));
    }
}
