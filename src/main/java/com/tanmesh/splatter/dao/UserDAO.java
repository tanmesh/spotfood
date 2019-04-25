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

    public List<UserPost> getAllPosts(String tag) {
        List<UserPost> userPosts = this.getDatastore().createQuery(UserPost.class).field("tags").contains(tag).asList();
        return userPosts;
    }

    public boolean userAlreadyExists(String emailId) {
        User user = this.getDatastore().createQuery(User.class).filter("emailId", emailId).get();
        if (user == null) {
            return false;
        }
        return true;
    }

    public User getUserFromEmailId(String emailId) {
        User user = this.getDatastore().createQuery(User.class).filter("emailId", emailId).get();
        return user;
    }
}
