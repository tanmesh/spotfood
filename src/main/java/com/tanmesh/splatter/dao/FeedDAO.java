package com.tanmesh.splatter.dao;

import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.entity.feed.Feed;
import com.tanmesh.splatter.wsRequestModel.SearchData;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FeedDAO extends BasicDAO<Feed, String> {
    public FeedDAO(Datastore ds) {
        super(ds);
    }

    public List<Feed> getFeed(String emailId, int offset, int limit) {
        Query query = this.getDatastore()
                .createQuery(Feed.class)
                .filter("emailId", emailId)
                .order("-creationTimestamp")
                .offset(offset)
                .limit(limit);

        List<Feed> feed = this.find(query).asList();

        return (feed == null) ? new ArrayList<>() : feed;
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

            if (distance < searchData.getRadius()) {
                post.setDistance((int) distance);
                out.add(post);
            }
        }

        return out;
    }

    public List<Feed> getAllFeeds(String emailId) {
        return this.getDatastore()
                .createQuery(Feed.class)
                .filter("emailId", emailId)
                .asList();
    }

    public Set<UserPost> getByTag(String emailId, String tag, SearchData searchData) {
        Set<UserPost> outFeeds = new HashSet<>();

        List<Feed> feeds = getAllFeeds(emailId);

        for (Feed feed : feeds) {
            List<UserPost> posts = this.getDatastore()
                    .createQuery(UserPost.class)
                    .field("_id").equal(feed.getUserPostId())
                    .field("tagList.name").equal(tag)
                    .order("-creationTimestamp")
                    .asList();
            if (searchData.getRadius() != -1) {
                outFeeds.addAll(getWithRadius(posts, searchData));
            } else {
                outFeeds.addAll(posts);
            }
        }

        return outFeeds;
    }

    public Set<UserPost> getNearBy(String emailId, SearchData searchData) {
        Set<UserPost> outFeeds = new HashSet<>();

        List<Feed> feeds = getAllFeeds(emailId);
        for (Feed feed : feeds) {
            List<UserPost> posts = this.getDatastore()
                    .createQuery(UserPost.class)
                    .filter("_id", feed.getUserPostId())
                    .order("-creationTimestamp")
                    .asList();

            outFeeds.addAll(getWithRadius(posts, searchData));
        }

        return outFeeds;
    }
}
