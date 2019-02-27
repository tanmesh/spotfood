package com.tanmesh.splatter.dao;

import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.entity.UserPost;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.ArrayList;
import java.util.List;

public class UserDAO extends BasicDAO<User, String> {
    public UserDAO(Datastore ds) {
        super(ds);
    }

    public User getUser(String idName, String id) {
        return this.getDatastore().createQuery(User.class).filter(idName, id).get();
    }

    public List<UserPost> getAllPost(String tag) {
        ArrayList<UserPost> userPosts = new ArrayList<>();
        // TODO: need to complete
        // List<UserPost> userPost = this.getDatastore().createQuery(UserPost.class);
        return userPosts;

    }
}
