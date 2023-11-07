package com.tanmesh.splatter.dao;

import com.google.common.collect.Sets;
import com.tanmesh.splatter.entity.Tag;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.wsRequestModel.SearchData;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserPostDAO extends BasicDAO<UserPost, String> {
    public UserPostDAO(Datastore ds) {
        super(ds);
    }

    public List<UserPost> getAllPost() {
        return this.getDatastore().createQuery(UserPost.class).asList();
    }

    public List<UserPost> getAllPostOfUser(String emailId) {
        Query<UserPost> query = this.getDatastore().createQuery(UserPost.class).filter("authorEmailId", emailId).order("-creationTimestamp");
        List<UserPost> post = this.find(query).asList();
        if (post == null) {
            return new ArrayList<>();
        }
        return post;
    }

    public UserPost getPostFromIds(ObjectId postId) {
        return this.getDatastore().createQuery(UserPost.class).field("postId").equal(postId).get();
    }

    public Set<UserPost> getAllPostForTags(Tag tag) {
        if (tag.getName() == null) {
            return null;
        }
        Set<UserPost> userPosts = Sets.newHashSet();
        List<UserPost> posts = this.getDatastore().createQuery(UserPost.class).field("tagList.name").equal(tag.getName()).order("-creationTimestamp").asList();
        if (posts != null) {
            userPosts.addAll(posts);
        }
        return userPosts;
    }

    private Set<UserPost> getWithRadius(List<UserPost> posts, SearchData searchData) {
        Set<UserPost> out = new HashSet<>();

        for (UserPost post : posts) {
            // Constants for Earth's radius in kilometers
            double EARTH_RADIUS = 6371.0;

            double[] postCoordinates = post.getLatLong().getCoordinates();

            double lat1 = Math.toRadians(postCoordinates[1]);
            double lon1 = Math.toRadians(postCoordinates[0]);
            double lat2 = Math.toRadians(searchData.getLatitude());
            double lon2 = Math.toRadians(searchData.getLongitude());

            double dlon = lon2 - lon1;
            double dlat = lat2 - lat1;

            double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            double distance = EARTH_RADIUS * c; // The distance in kilometers

            System.out.println("PostId: " + post.getPostId());
            System.out.println("Distance: " + distance + " kilometers");
            System.out.println("Radius: " + searchData.getRadius() + " kilometers");

            if (distance < searchData.getRadius()) {
                System.out.println("In");
                post.setDistance((int) distance);
                out.add(post);
            }
        }

        return out;
    }

    public Set<UserPost> getNearBy(SearchData searchData) {
        List<UserPost> posts = this.getDatastore()
                .createQuery(UserPost.class)
                .order("-creationTimestamp")
                .asList();

        return getWithRadius(posts, searchData);


//        Query<UserPost> query = this.getDatastore().createQuery(UserPost.class).field("tagList").contains(tagName);
//
//        Iterator<UserPost> userPosts = this.getDatastore()
//                .createAggregation(UserPost.class)
//                .geoNear(GeoNear.builder("distance")
//                        .setNear(latitude, longitude)
//                        .setSpherical(true)
//                        .build())
//                .match(query)
//                .aggregate(UserPost.class);
//
////        userPosts.cursor().forEachRemaining(System.out::println);
//
//        System.out.println(userPosts);
//
//        Set<UserPost> userPostSet = new HashSet<>();
//        while (userPosts.hasNext()) {
//            userPostSet.add(userPosts.next());
//        }
//        return userPostSet;

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

    public Set<UserPost> getByTag(String tag, SearchData searchData) {
        List<UserPost> posts = this.getDatastore()
                .createQuery(UserPost.class)
                .field("tagList.name").equal(tag)
                .order("-creationTimestamp")
                .asList();

        if (searchData.getRadius() != -1) {
            return getWithRadius(posts, searchData);
        }

        return new HashSet<>(posts);
    }
}