package com.tanmesh.splatter.authentication;

public class UserSession {
    private String accessToken;
    private String emailId;

    public UserSession() {
    }

    public UserSession(String emailId) {
        this.emailId = emailId;
    }

    public UserSession(String accessToken, String emailId) {
        this.accessToken = accessToken;
        this.emailId = emailId;
    }

    public String getAccessToken() {

        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
