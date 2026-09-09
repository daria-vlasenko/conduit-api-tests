package com.conduit.api.steps;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.oneOf;

import com.conduit.api.models.Comment;
import com.conduit.api.models.CommentRequest;
import com.conduit.api.models.CommentResponse;
import com.conduit.api.models.CommentsResponse;
import com.conduit.api.specs.Specs;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class CommentSteps {

    @Step("Add comment to article {slug}")
    public Response addComment(String token, String slug, Comment comment) {
        return given()
                .spec(Specs.authorizedRequest(token))
                .body(CommentRequest.of(comment))
                .when()
                .post("/articles/{slug}/comments", slug);
    }

    @Step("Add comment to article {slug} and return created entity")
    public Comment addCommentSuccessfully(String token, String slug, Comment comment) {
        return addComment(token, slug, comment)
                .then()
                .statusCode(oneOf(200, 201))
                .extract()
                .as(CommentResponse.class)
                .getComment();
    }

    @Step("Get comments of article {slug} as anonymous user")
    public CommentsResponse getComments(String slug) {
        return given()
                .spec(Specs.request())
                .when()
                .get("/articles/{slug}/comments", slug)
                .then()
                .spec(Specs.jsonResponse(200))
                .extract()
                .as(CommentsResponse.class);
    }

    @Step("Add comment to article {slug} without authorization")
    public Response addCommentWithoutAuthorization(String slug, Comment comment) {
        return given()
                .spec(Specs.request())
                .body(CommentRequest.of(comment))
                .when()
                .post("/articles/{slug}/comments", slug);
    }
}
