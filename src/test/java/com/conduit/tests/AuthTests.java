package com.conduit.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.oneOf;

import com.conduit.api.models.ErrorResponse;
import com.conduit.api.models.User;
import com.conduit.api.models.UserResponse;
import com.conduit.data.TestDataFactory;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Conduit API")
@Feature("Authentication")
class AuthTests extends BaseTest {

    @Test
    @Story("Registration")
    @DisplayName("New user is registered and receives a token")
    void registersNewUser() {
        User newUser = TestDataFactory.randomUser();

        UserResponse response = authSteps.registerUser(newUser)
                .then()
                .statusCode(oneOf(200, 201))
                .extract()
                .as(UserResponse.class);

        assertThat(response.getUser().getEmail(), equalTo(newUser.getEmail()));
        assertThat(response.getUser().getUsername(), equalTo(newUser.getUsername()));
        assertThat(response.getUser().getToken(), is(notNullValue()));
    }

    @Test
    @Story("Registration")
    @DisplayName("Registration with an already taken email is rejected")
    void rejectsRegistrationWithTakenEmail() {
        User duplicate = TestDataFactory.randomUser();
        duplicate.setEmail(currentUser.getEmail());

        Response response = authSteps.registerUser(duplicate);

        assertThat(response.statusCode(), is(oneOf(409, 422)));
        assertThat(response.as(ErrorResponse.class).getErrors(), is(not(anEmptyMap())));
    }

    @Test
    @Story("Login")
    @DisplayName("Registered user logs in with valid credentials")
    void logsInWithValidCredentials() {
        String token = authSteps.loginAndGetToken(currentUser.getEmail(), currentUser.getPassword());

        assertThat(token, is(notNullValue()));
        assertThat(token.length(), greaterThanOrEqualTo(10));
        authSteps.verifyCurrentUser(token, currentUser.getUsername());
    }

    @Test
    @Story("Login")
    @DisplayName("Login with invalid credentials is rejected")
    void rejectsLoginWithInvalidCredentials() {
        Response response = authSteps.loginUser(currentUser.getEmail(), "definitely-wrong-password");

        assertThat(response.statusCode(), is(oneOf(401, 403, 422)));
        assertThat(response.as(ErrorResponse.class).getErrors(), is(not(anEmptyMap())));
    }

    @Test
    @Story("Login")
    @DisplayName("Login with unknown email is rejected")
    void rejectsLoginWithUnknownEmail() {
        User unknown = TestDataFactory.randomUser();

        Response response = authSteps.loginUser(unknown.getEmail(), unknown.getPassword());

        assertThat(response.statusCode(), is(oneOf(401, 403, 422)));
    }
}
