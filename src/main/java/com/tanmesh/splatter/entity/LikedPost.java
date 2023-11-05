package com.tanmesh.splatter.entity;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "likedPost", noClassnameStored = true)
public class LikedPost {
    @Id
    private String key;

    public LikedPost() {
    }

    public LikedPost(ObjectId postId, String emailId) {
        this.key = postId + "-" + emailId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
