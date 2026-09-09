package com.conduit.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Article {

    private String slug;
    private String title;
    private String description;
    private String body;
    private List<String> tagList;
    private String createdAt;
    private String updatedAt;
    private Boolean favorited;
    private Integer favoritesCount;
    private Author author;

    public static Builder builder() {
        return new Builder();
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getFavorited() {
        return favorited;
    }

    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    public Integer getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(Integer favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public static final class Builder {

        private final Article article = new Article();

        public Builder title(String title) {
            article.setTitle(title);
            return this;
        }

        public Builder description(String description) {
            article.setDescription(description);
            return this;
        }

        public Builder body(String body) {
            article.setBody(body);
            return this;
        }

        public Builder tagList(List<String> tagList) {
            article.setTagList(tagList);
            return this;
        }

        public Article build() {
            return article;
        }
    }
}
