package com.tanmesh.spotfood.service;

import com.tanmesh.spotfood.authentication.UserSession;

/**
 * Created by tanmesh
 * Date: 2019-07-14
 * Time: 07:17
 */
public interface AccessTokenService {
//    UserSession createAccessToken(UserSession userSession);

    boolean saveAccessToken(UserSession userSession);

    UserSession getUserSessionFromAccessToken(String accessToken);

    boolean isValidToken(String accessToken);

    boolean removeAccessToken(String accessToken);

    void removeUser(String userName);
}
