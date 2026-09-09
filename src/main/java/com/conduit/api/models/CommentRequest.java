package com.conduit.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequest {

    private Comment comment;

    public CommentRequest() {
    }

    public CommentRequest(Comment comment) {
        this.comment = comment;
    }

    public static CommentRequest of(Comment comment) {
        return new CommentRequest(comment);
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
