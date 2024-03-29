package com.tanmesh.spotfood.authentication;

import com.google.common.base.Optional;
import com.tanmesh.spotfood.service.AccessTokenService;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

/**
 * Created by tanmesh
 * Date: 2019-09-05
 * Time: 12:52
 */
public class AccessTokenAuthenticator implements Authenticator<AccessTokenCredentials, UserSession> {
    private AccessTokenService accessTokenService;
    public AccessTokenAuthenticator(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }
    @Override
    public Optional<UserSession> authenticate(AccessTokenCredentials accessTokenCredentials) throws AuthenticationException {
        if (accessTokenService.isValidToken(accessTokenCredentials.getToken())) {
            UserSession agent = accessTokenService.getUserSessionFromAccessToken(accessTokenCredentials.getToken());
            return Optional.fromNullable(agent);
        } else {
            throw new AuthenticationException("Invalid credentials");
        }
    }
}
