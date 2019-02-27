package com.tanmesh.splatter.dao;

import com.tanmesh.splatter.entity.UserPost;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UserPostDAO extends BasicDAO<UserPost, String> {
    public UserPostDAO(Datastore ds) {
        super(ds);
    }

    public List<UserPost> getAllPost(String idName, String id) {
        Query<UserPost> query = this.getDatastore().createQuery(UserPost.class).filter(idName, id);
        List<UserPost> userPostList = this.find(query).asList();
        if (userPostList == null) {
            return new ArrayList<>();
        }
        return userPostList;
    }

    public UserPost getPost(String idName, String id) {
        return this.getDatastore().createQuery(UserPost.class).filter(idName, id).get();
    }
}
