package com.tanmesh.spotfood.authentication;

/**
 * Created by tanmesh
 * Date: 2019-09-05
 * Time: 12:41
 */
public class AccessTokenCredentials {
    private String token;
    public AccessTokenCredentials(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
