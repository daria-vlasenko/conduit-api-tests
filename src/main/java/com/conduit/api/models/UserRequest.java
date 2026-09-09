package com.conduit.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {

    private User user;

    public UserRequest() {
    }

    public UserRequest(User user) {
        this.user = user;
    }

    public static UserRequest of(User user) {
        return new UserRequest(user);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
