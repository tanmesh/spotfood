package com.tanmesh.splatter.dao;

import com.tanmesh.splatter.entity.Auth;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class AuthDAO extends BasicDAO<Auth, String> {
    public AuthDAO(Datastore ds) {
        super(ds);
    }

    public String getAccessToken(String userId) {
        Auth auth =  this.getDatastore().createQuery(Auth.class).filter("userId", userId).get();
        return auth.getAccessToken();
    }

    public String getUserId(String accessToken) {
        Auth auth =  this.getDatastore().createQuery(Auth.class).filter("accessToken",accessToken).get();
        return auth.getUserId();
    }

    public boolean isValidToken(String accessToken) {
        Auth auth =  this.getDatastore().createQuery(Auth.class).filter("accessToken",accessToken).get();
        if (auth != null) {
            return true;
        }
        return false;
    }

    public void removeAccessToken(String userId, String accessToken) {
        Auth auth =  this.getDatastore().createQuery(Auth.class).filter("accessToken",accessToken).filter("userId", userId).get();
        this.getDatastore().delete(auth);
    }
}
