package com.tanmesh.splatter.service;

import com.tanmesh.splatter.authentication.UserSession;

/**
 * Created by tanmesh
 * Date: 2019-07-14
 * Time: 07:17
 */
public interface AccessTokenService {
    UserSession createAccessToken(UserSession userSession);

    UserSession saveAccessToken(UserSession userSession);

    UserSession getUserFromAccessToken(String accessToken);

    boolean isValidToken(String accessToken);

    void removeAccessToken(String accessToken);

    void removeUser(String userName);
}
