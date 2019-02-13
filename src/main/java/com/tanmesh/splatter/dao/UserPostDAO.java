package com.tanmesh.splatter.dao;

import com.tanmesh.splatter.entity.UserPost;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class UserPostDAO extends BasicDAO<UserPost, String> {
    public UserPostDAO(Datastore ds) { super(ds); }
}
