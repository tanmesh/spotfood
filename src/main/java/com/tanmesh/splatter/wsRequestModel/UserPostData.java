package com.tanmesh.splatter.wsRequestModel;

import com.tanmesh.splatter.entity.UserPost;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserPostData {
    private String postId;
    private Set<String> tagList = new HashSet<>();
    private String locationName;
    private String authorEmailId;
    private String authorName;
    private int upVotes;
    private List<String> imgUrl;
    private Double latitude;
    private Double longitude;
    private long creationTimestamp;
    private boolean liked;

    private int distance;

    public UserPostData() {
    }

    public UserPostData(UserPost userPost, int distance) {
        this.postId = userPost.getPostId().toString();
        this.tagList = userPost.getTagsString();
        this.locationName = userPost.getLocationName();
        this.authorEmailId = userPost.getAuthorEmailId();
        this.upVotes = userPost.getUpVotes();
        this.imgUrl = userPost.getImgUrl();
        this.latitude = userPost.getLatLong().getCoordinates()[1];
        this.longitude = userPost.getLatLong().getCoordinates()[0];
        this.creationTimestamp = userPost.getCreationTimestamp();
        this.liked = userPost.getLiked();
        this.authorName = userPost.getAuthorName();
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
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

    public List<String> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<String> imgUrl) {
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

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
