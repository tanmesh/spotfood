package com.tanmesh.splatter.entity;

import org.mongodb.morphia.annotations.Id;

public class Auth {
    @Id
    String accessToken;
    String userId;

    public Auth() {
    }

    public Auth(String accessToken, String userId) {
        this.accessToken = accessToken;
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
