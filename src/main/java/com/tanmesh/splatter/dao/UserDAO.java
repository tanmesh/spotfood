package com.tanmesh.splatter.dao;

import com.tanmesh.splatter.entity.User;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import java.util.List;

public class UserDAO extends BasicDAO<User, String> {
    public UserDAO(Datastore ds) {
        super(ds);
    }

    public User getUserByEmailId(String emailId) {
        return this.getDatastore()
                .createQuery(User.class)
                .filter("emailId", emailId)
                .get();
    }

    public List<User> getAllUser() {
        Query<User> query = this.getDatastore().createQuery(User.class);
        return query.asList();
    }

//    public List<UserPost> getAllFeed(String tag) {
////        ArrayList<UserPost> userPosts = new ArrayList<>();
//        List<UserPost> userPosts = this.getDatastore().createQuery(UserPost.class).asList();
//        return userPosts;
//    }
}
