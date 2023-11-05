package com.tanmesh.splatter.dao;

import com.google.common.collect.Sets;
import com.tanmesh.splatter.entity.Tag;
import com.tanmesh.splatter.entity.UserPost;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.aggregation.GeoNear;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import java.util.*;

public class UserPostDAO extends BasicDAO<UserPost, String> {
    public UserPostDAO(Datastore ds) {
        super(ds);
    }

    public List<UserPost> getAllPost() {
        return this.getDatastore().createQuery(UserPost.class).asList();
    }

    public List<UserPost> getAllPostOfUser(String emailId) {
        Query<UserPost> query = this.getDatastore()
                .createQuery(UserPost.class)
                .filter("authorEmailId", emailId)
                .order("-creationTimestamp");
        List<UserPost> post = this.find(query).asList();
        if (post == null) {
            return new ArrayList<>();
        }
        return post;
    }

    public UserPost getPostFromIds(String postId) {
        return this.getDatastore()
                .createQuery(UserPost.class)
                .field("postId")
                .equal(new ObjectId(postId)).get();
    }

    public Set<UserPost> getAllPostForTags(Tag tag) {
        System.out.println(tag.getName());
        Set<UserPost> userPosts = Sets.newHashSet();
        List<UserPost> posts = this.getDatastore().
                createQuery(UserPost.class)
                .field("tagList.name").equal(tag.getName())
                .order("-creationTimestamp")
                .asList();
        if (posts != null) {
            userPosts.addAll(posts);
        }
        return userPosts;
    }

    public Set<UserPost> getNearBy(String tagName, double latitude, double longitude) {
        Query<UserPost> query = this.getDatastore()
                .createQuery(UserPost.class)
                .field("tags")
                .contains(tagName);

        Iterator<UserPost> userPosts = this.getDatastore().createAggregation(UserPost.class)
                .geoNear(GeoNear.builder("distance")
                        .setNear(latitude, longitude)
                        .setSpherical(true)
                        .build()).match(query)
                .aggregate(UserPost.class);

        Set<UserPost> userPostSet = new HashSet<>();
        while (userPosts.hasNext()) {

            userPostSet.add(userPosts.next());
        }
        return userPostSet;

//        Query<UserPost> query = userPostDAO.getDs().createQuery(UserPost.class).field("tagIdList").equal(tagId).limit(50);
//        Iterator<UserPost> userPostIterator = userPostDAO.getDs().createAggregation(UserPost.class)
//                .geoNear(GeoNear.builder("distance")
//                        .setNear(latitude, longitude)
//                        .setSpherical(true)
//                        .build()).match(query)
//                .aggregate(UserPost.class);
//
//
//        Set<UserPostResponse> userPostResponseSet = Sets.newHashSet();
//        while (userPostIterator.hasNext()) {
//            UserPost userPost = userPostIterator.next();
//            UserPostResponse userPostResponse = getUserPostResponse(userPost);
//            userPostResponseSet.add(userPostResponse);
//        }
//
//        return userPostResponseSet;
    }
}