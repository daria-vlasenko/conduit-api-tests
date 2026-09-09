package com.conduit.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticlesResponse {

    private List<Article> articles;
    private Integer articlesCount;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Integer getArticlesCount() {
        return articlesCount;
    }

    public void setArticlesCount(Integer articlesCount) {
        this.articlesCount = articlesCount;
    }
}
