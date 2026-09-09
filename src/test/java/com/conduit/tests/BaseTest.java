package com.conduit.tests;

import com.conduit.api.models.User;
import com.conduit.api.steps.ArticleSteps;
import com.conduit.api.steps.AuthSteps;
import com.conduit.api.steps.CommentSteps;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {

    protected final AuthSteps authSteps = new AuthSteps();
    protected final ArticleSteps articleSteps = new ArticleSteps();
    protected final CommentSteps commentSteps = new CommentSteps();

    protected User currentUser;
    protected String authToken;

    @BeforeEach
    void authenticate() {
        currentUser = authSteps.registerNewUser();
        authToken = authSteps.loginAndGetToken(currentUser.getEmail(), currentUser.getPassword());
    }
}
