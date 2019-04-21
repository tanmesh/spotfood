package com.tanmesh.splatter.authentication;

import com.google.common.base.Optional;

import javax.inject.Inject;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

public class AccessTokenAuthenticator implements Authenticator<AccessTokenCredentials,UserSession> {

    @Inject
    public AccessTokenAuthenticator() {
    }


    @Override
    public Optional<UserSession> authenticate(AccessTokenCredentials credentials) throws AuthenticationException {
        if (AccessTokenService.isValidToken(credentials.getToken())) {
            UserSession agent = AccessTokenService.getUserFromAccessToken(credentials.getToken());
            return Optional.fromNullable(agent);
        } else {
            throw new AuthenticationException("Invalid credentials");
        }
    }
}
