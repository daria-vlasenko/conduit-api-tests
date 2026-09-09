package com.conduit.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.oneOf;

import com.conduit.api.models.Article;
import com.conduit.api.models.Comment;
import com.conduit.data.TestDataFactory;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Conduit API")
@Feature("Comments")
class CommentTests extends BaseTest {

    private Article article;

    @BeforeEach
    void createArticle() {
        article = articleSteps.createArticleSuccessfully(authToken, TestDataFactory.randomArticle());
    }

    @AfterEach
    void removeArticle() {
        articleSteps.deleteArticle(authToken, article.getSlug());
    }

    @Test
    @Story("Create comment")
    @DisplayName("Authorized user comments on an article")
    void createsComment() {
        Comment comment = TestDataFactory.randomComment();

        Comment created = commentSteps.addCommentSuccessfully(authToken, article.getSlug(), comment);

        assertThat(created.getId(), is(notNullValue()));
        assertThat(created.getBody(), equalTo(comment.getBody()));
        assertThat(created.getAuthor().getUsername(), equalTo(currentUser.getUsername()));
    }

    @Test
    @Story("Read comments")
    @DisplayName("Anonymous user reads comments of an article")
    void readsCommentsAsAnonymousUser() {
        Comment created = commentSteps.addCommentSuccessfully(authToken, article.getSlug(),
                TestDataFactory.randomComment());

        List<Comment> comments = commentSteps.getComments(article.getSlug()).getComments();

        assertThat(comments.stream().map(Comment::getId).toList(), hasItem(created.getId()));
    }

    @Test
    @Story("Create comment")
    @DisplayName("Anonymous user cannot comment on an article")
    void rejectsCommentWithoutAuthorization() {
        Response response = commentSteps.addCommentWithoutAuthorization(article.getSlug(),
                TestDataFactory.randomComment());

        assertThat(response.statusCode(), is(oneOf(401, 403, 422)));
    }
}
