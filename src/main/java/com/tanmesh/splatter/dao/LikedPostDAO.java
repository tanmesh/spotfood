package com.tanmesh.splatter.dao;

import com.tanmesh.splatter.entity.LikedPost;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

public class LikedPostDAO extends BasicDAO<LikedPost, String> {
    public LikedPostDAO(Datastore ds) {
        super(ds);
    }

    public boolean exist(String emailId, ObjectId postId) {
        String compoundKey = postId + "-" + emailId;
        LikedPost likedPost = this.getDatastore()
                .createQuery(LikedPost.class)
                .field("key").equal(compoundKey)
                .get();
        return likedPost != null;
    }

    public void dislikedPost(String emailId, ObjectId postId) {
        String compoundKey = postId + "-" + emailId;
        Query<LikedPost> query = this.getDatastore()
                .createQuery(LikedPost.class)
                .field("key").equal(compoundKey);

        this.getDatastore().delete(query);
    }
}
