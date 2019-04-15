package com.tanmesh.splatter.wsResponseModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tanmesh.splatter.entity.User;

import javax.ws.rs.core.Response;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAuthResponse {
    private String accessToken;
    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
    private String message;

    public UserAuthResponse() {
    }

    public UserAuthResponse(Response response) {

    }

    public UserAuthResponse(User user, String token, Response response) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.emailId = user.getEmailId();
        this.password = user.getPassword();
        this.message = response.toString();
        this.accessToken = token;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}