package com.tanmesh.splatter.wsRequestModel;

import java.util.HashSet;
import java.util.Set;

public class UserPostData {
    private String postId;
    private Set<String> tagList = new HashSet<>();
    private String locationName;
    private String authorEmailId;
    private int upvotes;
    private String imgUrl;
    private String fileExtenstion;
    private Double latitude;
    private Double longitude;
    private long lastTimestamp;
    private boolean liked;

    public UserPostData() {
    }

    public UserPostData(String postId, Set<String> tagList, String locationName, String authorEmailId, int upvotes, String imgUrl, Double latitude, Double longitude, long lastTimestamp) {
        this.postId = postId;
        this.tagList = tagList;
        this.locationName = locationName;
        this.authorEmailId = authorEmailId;
        this.upvotes = upvotes;
        this.imgUrl = imgUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastTimestamp = lastTimestamp;
        this.liked = false;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getFileExtenstion() {
        return fileExtenstion;
    }

    public void setFileExtenstion(String fileExtenstion) {
        this.fileExtenstion = fileExtenstion;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Set<String> getTagList() {
        return tagList;
    }

    public void setTagList(Set<String> tagList) {
        this.tagList = tagList;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAuthorEmailId() {
        return authorEmailId;
    }

    public void setAuthorEmailId(String authorEmailId) {
        this.authorEmailId = authorEmailId;
    }

    public int getUpvotes(int upVotes) {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public long getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(long lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
