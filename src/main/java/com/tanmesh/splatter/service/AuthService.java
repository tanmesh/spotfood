package com.tanmesh.splatter.service;

import com.tanmesh.splatter.entity.User;

import java.util.HashMap;
import java.util.UUID;

public class AuthService {
    static private HashMap<String, String> userVsAccessTokenMap = new HashMap<>();
    static private HashMap<String, String> accessTokenVsUserMap = new HashMap<>();

    public AuthService() {
    }

    static public void addNewAccessToken(User user) {
        String emailId = user.getEmailId();
        String token =  UUID.randomUUID().toString();
        userVsAccessTokenMap.put(emailId, token);
        accessTokenVsUserMap.put(token, emailId);
    }

    static public String getAccessToken(User user) {
        String emailId = user.getEmailId();
        String token = "";
        if (!userVsAccessTokenMap.isEmpty()) {
            token = userVsAccessTokenMap.get(emailId);
        }
        return token;
    }
    
    static public String getUserEmailId(String accessToken) {
        String emailId = "";
        if (!accessTokenVsUserMap.isEmpty()) {
            emailId = accessTokenVsUserMap.get(accessToken);
        }
        return emailId;
    }

    static public boolean isValidToken(String accessToken) {
        if (accessTokenVsUserMap.containsKey(accessToken)) {
            return true;
        }
        return false;
    }

    static public boolean removeAccessToken(String emailId, String accessToken) {
        if (accessTokenVsUserMap.containsKey(accessToken)) {
            accessTokenVsUserMap.remove(accessToken);
            userVsAccessTokenMap.remove(emailId);
            return true;
        }
        return false;
    }

}
