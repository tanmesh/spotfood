package com.tanmesh.splatter.service;

import java.util.HashMap;
import java.util.UUID;

public class UserAuthService {
    private HashMap<String, String> accessTokenMap;

    public UserAuthService() {
        this.accessTokenMap = new HashMap<>();
    }

    public String addNewAccessToken(String emailId) {
        String token =  UUID.randomUUID().toString();
        accessTokenMap.put(emailId, token);
        return token;
    }

}
