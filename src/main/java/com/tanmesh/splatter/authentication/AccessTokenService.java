package com.tanmesh.splatter.authentication;

import com.tanmesh.splatter.service.AuthService;

public class AccessTokenService {

    public static UserSession getUserFromAccessToken(String token) {
        String emailId = AuthService.getUserEmailId(token);
        UserSession userSession = new UserSession(token,emailId);
        return userSession;
    }

    public static boolean isValidToken(String token) {
        return AuthService.isValidToken(token);
    }
}
