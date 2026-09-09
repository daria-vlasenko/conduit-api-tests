package com.conduit.api.steps;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.oneOf;

import com.conduit.api.models.Article;
import com.conduit.api.models.ArticleRequest;
import com.conduit.api.models.ArticleResponse;
import com.conduit.api.models.ArticlesResponse;
import com.conduit.api.specs.Specs;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class ArticleSteps {

    @Step("Create article {article.title}")
    public Response createArticle(String token, Article article) {
        return given()
                .spec(Specs.authorizedRequest(token))
                .body(ArticleRequest.of(article))
                .when()
                .post("/articles");
    }

    @Step("Create article {article.title} and return created entity")
    public Article createArticleSuccessfully(String token, Article article) {
        return createArticle(token, article)
                .then()
                .statusCode(oneOf(200, 201))
                .extract()
                .as(ArticleResponse.class)
                .getArticle();
    }

    @Step("Create article without authorization")
    public Response createArticleWithoutAuthorization(Article article) {
        return given()
                .spec(Specs.request())
                .body(ArticleRequest.of(article))
                .when()
                .post("/articles");
    }

    @Step("Get article {slug} as anonymous user")
    public Response getArticle(String slug) {
        return given()
                .spec(Specs.request())
                .when()
                .get("/articles/{slug}", slug);
    }

    @Step("Get global articles feed with limit {limit}")
    public ArticlesResponse getArticles(int limit) {
        return given()
                .spec(Specs.request())
                .queryParam("limit", limit)
                .when()
                .get("/articles")
                .then()
                .spec(Specs.jsonResponse(200))
                .extract()
                .as(ArticlesResponse.class);
    }

    @Step("Delete article {slug}")
    public Response deleteArticle(String token, String slug) {
        return given()
                .spec(Specs.authorizedRequest(token))
                .when()
                .delete("/articles/{slug}", slug);
    }

    @Step("Delete article {slug} as author")
    public void deleteArticleSuccessfully(String token, String slug) {
        deleteArticle(token, slug)
                .then()
                .statusCode(oneOf(200, 204));
    }
}
