package com.tanmesh.splatter.dao;

import com.google.common.collect.Sets;
import com.tanmesh.splatter.entity.UserPost;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserPostDAO extends BasicDAO<UserPost, String> {
    public UserPostDAO(Datastore ds) {
        super(ds);
    }

    public List<UserPost> getAllPostOfUser(String emailId) {
        Query<UserPost> query = this.getDatastore().createQuery(UserPost.class).filter("authorEmailId", emailId);
        List<UserPost> userPostList = this.find(query).asList();
        if (userPostList == null) {
            return new ArrayList<>();
        }
        return userPostList;
    }

    public UserPost getPost(String idName, String id) {
        return this.getDatastore().createQuery(UserPost.class).filter(idName, id).get();
    }

    public Set<UserPost> getAllPostForTags(Set<String> followedTags) {
        Set<UserPost> userPosts = Sets.newHashSet();
        for (String tag : followedTags) {
            List<UserPost> posts = this.getDatastore().createQuery(UserPost.class).field("tags").contains(tag).asList();
            if (posts != null) {
                userPosts.addAll(posts);
            }
        }
        return userPosts;
    }
}
