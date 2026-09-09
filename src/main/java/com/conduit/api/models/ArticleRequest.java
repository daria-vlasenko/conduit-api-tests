package com.conduit.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleRequest {

    private Article article;

    public ArticleRequest() {
    }

    public ArticleRequest(Article article) {
        this.article = article;
    }

    public static ArticleRequest of(Article article) {
        return new ArticleRequest(article);
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
