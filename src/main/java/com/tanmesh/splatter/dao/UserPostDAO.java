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

    public List<UserPost> getAllPost(String authorName) {
        List<UserPost> userPostList = new ArrayList<>();
        Query<UserPost> query = this.getDatastore().createQuery(UserPost.class).filter("authorName", authorName);
        userPostList = this.find(query).asList();
        if (userPostList == null) {
            return new ArrayList<>();
        }
        return userPostList;
    }

    public UserPost getPost(String condition, String matchId) {
        return this.getDatastore().createQuery(UserPost.class).filter(condition, matchId).get();
    }

}
