package com.conduit.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.oneOf;

import com.conduit.api.models.Article;
import com.conduit.api.models.ArticleResponse;
import com.conduit.api.models.User;
import com.conduit.data.TestDataFactory;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Conduit API")
@Feature("Articles")
class ArticleTests extends BaseTest {

    @Test
    @Story("Create article")
    @DisplayName("Authorized user creates an article")
    void createsArticleAsAuthorizedUser() {
        Article article = TestDataFactory.randomArticle();

        Article created = articleSteps.createArticleSuccessfully(authToken, article);

        assertThat(created.getSlug(), is(notNullValue()));
        assertThat(created.getTitle(), equalTo(article.getTitle()));
        assertThat(created.getBody(), equalTo(article.getBody()));
        assertThat(created.getAuthor().getUsername(), equalTo(currentUser.getUsername()));

        articleSteps.deleteArticleSuccessfully(authToken, created.getSlug());
    }

    @Test
    @Story("Create article")
    @DisplayName("Anonymous user cannot create an article")
    void rejectsArticleCreationWithoutAuthorization() {
        Response response = articleSteps.createArticleWithoutAuthorization(TestDataFactory.randomArticle());

        assertThat(response.statusCode(), is(oneOf(401, 403, 422)));
    }

    @Test
    @Story("Read article")
    @DisplayName("Anonymous user reads a published article")
    void readsArticleAsAnonymousUser() {
        Article created = articleSteps.createArticleSuccessfully(authToken, TestDataFactory.randomArticle());

        ArticleResponse response = articleSteps.getArticle(created.getSlug())
                .then()
                .statusCode(200)
                .extract()
                .as(ArticleResponse.class);

        assertThat(response.getArticle().getSlug(), equalTo(created.getSlug()));
        assertThat(response.getArticle().getTitle(), equalTo(created.getTitle()));

        articleSteps.deleteArticleSuccessfully(authToken, created.getSlug());
    }

    @Test
    @Story("Delete article")
    @DisplayName("Author deletes own article")
    void deletesOwnArticle() {
        Article created = articleSteps.createArticleSuccessfully(authToken, TestDataFactory.randomArticle());

        articleSteps.deleteArticleSuccessfully(authToken, created.getSlug());

        assertThat(articleSteps.getArticle(created.getSlug()).statusCode(), is(oneOf(404, 422)));
    }

    @Test
    @Story("Delete article")
    @DisplayName("Foreign user cannot delete someone else's article")
    void rejectsDeletionOfForeignArticle() {
        Article created = articleSteps.createArticleSuccessfully(authToken, TestDataFactory.randomArticle());
        User intruder = authSteps.registerNewUser();

        Response response = articleSteps.deleteArticle(intruder.getToken(), created.getSlug());

        assertThat(response.statusCode(), is(oneOf(401, 403)));
        assertThat(articleSteps.getArticle(created.getSlug()).statusCode(), is(200));

        articleSteps.deleteArticleSuccessfully(authToken, created.getSlug());
    }
}
