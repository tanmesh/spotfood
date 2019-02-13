package com.tanmesh.splatter.dao;

import com.tanmesh.splatter.entity.User;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class UserDAO extends BasicDAO<User, String> {
    public UserDAO(Datastore ds) {
        super(ds);
    }
}
