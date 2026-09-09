package com.conduit.api.steps;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.oneOf;

import com.conduit.api.models.User;
import com.conduit.api.models.UserRequest;
import com.conduit.api.models.UserResponse;
import com.conduit.api.specs.Specs;
import com.conduit.data.TestDataFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class AuthSteps {

    @Step("Register user with email {user.email}")
    public Response registerUser(User user) {
        return given()
                .spec(Specs.request())
                .body(UserRequest.of(user))
                .when()
                .post("/users");
    }

    @Step("Register new user and return credentials with token")
    public User registerNewUser() {
        User user = TestDataFactory.randomUser();
        UserResponse response = registerUser(user)
                .then()
                .statusCode(oneOf(200, 201))
                .extract()
                .as(UserResponse.class);
        user.setToken(response.getUser().getToken());
        return user;
    }

    @Step("Log in with email {email}")
    public Response loginUser(String email, String password) {
        User credentials = User.builder()
                .email(email)
                .password(password)
                .build();
        return given()
                .spec(Specs.request())
                .body(UserRequest.of(credentials))
                .when()
                .post("/users/login");
    }

    @Step("Log in with email {email} and return JWT token")
    public String loginAndGetToken(String email, String password) {
        return loginUser(email, password)
                .then()
                .spec(Specs.jsonResponse(200))
                .extract()
                .as(UserResponse.class)
                .getUser()
                .getToken();
    }

    @Step("Get current user by token")
    public Response getCurrentUser(String token) {
        return given()
                .spec(Specs.authorizedRequest(token))
                .when()
                .get("/user");
    }

    @Step("Verify current user is {expectedUsername}")
    public void verifyCurrentUser(String token, String expectedUsername) {
        getCurrentUser(token)
                .then()
                .spec(Specs.jsonResponse(200))
                .body("user.username", is(expectedUsername));
    }
}
