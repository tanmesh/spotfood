package com.tanmesh.splatter.service;

import com.tanmesh.splatter.entity.User;

import java.util.HashMap;
import java.util.UUID;

public class UserAuthService {
    static private HashMap<String, String> accessTokenMap = new HashMap<>();

    public UserAuthService() {
    }

    static public void addNewAccessToken(User user) {
        String emailId = user.getEmailId();
        String token =  UUID.randomUUID().toString();
        accessTokenMap.put(emailId, token);
    }

    static public String getAccessToken(User user) {
        String emailId = user.getEmailId();
        String token = "";
        if (accessTokenMap.isEmpty()) {
            token = accessTokenMap.get(emailId);
        }
        return token;
    }

}
