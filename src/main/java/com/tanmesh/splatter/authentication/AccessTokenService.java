package com.tanmesh.splatter.authentication;

import java.util.HashMap;

public class AccessTokenService {
    static private HashMap<String, String> userVsAccessTokenMap = new HashMap<>();
    static private HashMap<String, String> accessTokenVsUserMap = new HashMap<>();


    static public UserSession getUserFromAccessToken(String token) {
        String emailId = getUserEmailId(token);
        UserSession userSession = new UserSession(token,emailId);
        return userSession;
    }

    static public boolean isValidToken(String token) {
        String emailId = getUserEmailId(token);
        if (!emailId.isEmpty()) {
            return true;
        }
        return false;
    }

    static public void addNewAccessToken(String emailId, String token) {
        // add in local cache
        userVsAccessTokenMap.put(emailId, token);
        accessTokenVsUserMap.put(token, emailId);
    }

    static public String getAccessToken(String emailId) {
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

    static public void removeAccessToken(String emailId, String accessToken) {
        if (accessTokenVsUserMap.containsKey(accessToken)) {
            accessTokenVsUserMap.remove(accessToken);
            userVsAccessTokenMap.remove(emailId);
        }
    }
}
