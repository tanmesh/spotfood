package com.tanmesh.splatter.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;
import java.util.Objects;

@Entity(value = "userPost_data", noClassnameStored = true)
public class UserPost {
    @Id
    private String postId;
    private List<String> tags;
    private String location;
    private String authorEmailId;
    private int upVotes;
    private String encodedImgString;

    public UserPost() {
    }

    public String getPostId() { return postId; }

    public void setPostId(String postId) { this.postId = postId; }

    public List<String> getTags() {
        return tags;
    }

    public String getEncodedImgString() {
        return encodedImgString;
    }

    public void setEncodedImgString(String encodedImgString) {
        this.encodedImgString = encodedImgString;
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

    public String getAuthorEmailId() {
        return authorEmailId;
    }

    public void setAuthorEmailId(String authorEmailId) {
        this.authorEmailId = authorEmailId;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPost userPost = (UserPost) o;
        return Objects.equals(postId, userPost.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId);
    }
}
