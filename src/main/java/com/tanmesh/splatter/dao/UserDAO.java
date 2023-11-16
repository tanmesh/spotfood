package com.tanmesh.splatter.dao;

import com.tanmesh.splatter.entity.User;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.List;

public class UserDAO extends BasicDAO<User, String> {
    public UserDAO(Datastore ds) {
        super(ds);
    }

    public String getUserName(String emailId) {
        User user = this.getDatastore()
                .createQuery(User.class)
                .filter("emailId", emailId)
                .get();

        return user.getFirstName() + " " + user.getLastName();
    }

    public User getUserByEmailId(String emailId) {
        return this.getDatastore()
                .createQuery(User.class)
                .filter("emailId", emailId)
                .get();
    }

    public List<User> getAllUser() {
        return this.getDatastore().createQuery(User.class).asList();
    }

    public List<User> getAllUserExcept(String emailId) {
        return this.getDatastore()
                .createQuery(User.class)
                .filter("emailId !=", emailId)
                .asList();
    }

//    public List<UserPost> getAllFeed(String tag) {
////        ArrayList<UserPost> userPosts = new ArrayList<>();
//        List<UserPost> userPosts = this.getDatastore().createQuery(UserPost.class).asList();
//        return userPosts;
//    }
}
