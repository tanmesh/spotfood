package com.tanmesh.splatter.authentication;

import com.google.common.base.Optional;

import javax.inject.Inject;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

public class AccessTokenAuthenticator implements Authenticator<AccessTokenCredentials,UserSession> {

    private AuthService authService;

    @Inject
    public AccessTokenAuthenticator() {
    }


    public AccessTokenAuthenticator(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Optional<UserSession> authenticate(AccessTokenCredentials credentials) throws AuthenticationException {
        if (authService.isValidToken(credentials.getToken())) {
            UserSession agent = authService.getUserFromAccessToken(credentials.getToken());
            return Optional.fromNullable(agent);
        } else {
            throw new AuthenticationException("Invalid credentials");
        }
    }
}
