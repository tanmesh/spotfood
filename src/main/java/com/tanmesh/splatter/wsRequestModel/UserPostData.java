package com.tanmesh.splatter.wsRequestModel;

import java.util.List;

public class UserPostData {
    private String postId;
    private List<String> tags;
    private String location;
    private String authorEmailId;
    private int upvotes;
    private String encodedImg;
    private String encodedImgFilePath;

    public String getEncodedImgFilePath() {
        return encodedImgFilePath;
    }

    public void setEncodedImgFilePath(String encodedImgFilePath) {
        this.encodedImgFilePath = encodedImgFilePath;
    }

    public String getEncodedImg() {
        return encodedImg;
    }

    public void setEncodedImg(String encodedImg) {
        this.encodedImg = encodedImg;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

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

    public String getAuthorEmailId() {
        return authorEmailId;
    }

    public void setAuthorEmailId(String authorEmailId) {
        this.authorEmailId = authorEmailId;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }
}
