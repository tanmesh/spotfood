package com.tanmesh.splatter.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

@Entity(value = "userPost_data", noClassnameStored = true)
public class UserPost {
    @Id
    private String postId;
    private List<String> tags;
    private String location;
    private String authorName;
    private int upVotes;
    private String encodedImg;

    public UserPost() {
    }

    public String getPostId() { return postId; }

    public void setPostId(String postId) { this.postId = postId; }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public String getEncodedImg() {
        return encodedImg;
    }

    public void setEncodedImg(String encodedImg) {
        this.encodedImg = encodedImg;
    }
}
