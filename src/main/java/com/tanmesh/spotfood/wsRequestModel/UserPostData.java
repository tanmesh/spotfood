package com.tanmesh.spotfood.wsRequestModel;

import com.tanmesh.spotfood.entity.UserPost;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserPostData {
    private String postId;
    private Set<String> tagList;
    private String restaurantName;
    private String restaurantId;
    private String authorEmailId;
    private String authorName;
    private int upVotes;
    private List<String> imgUrl;
    private Double latitude;
    private Double longitude;
    private String address;
    private long creationTimestamp;
    private boolean liked;
    private int distance;

    public UserPostData() {
    }

    public UserPostData(UserPost userPost, int distance) {
        this.postId = userPost.getPostId().toString();
        this.tagList = userPost.getTagsString();
        this.restaurantId = userPost.getRestaurantId();
        this.restaurantName = userPost.getRestaurantName();
        this.authorEmailId = userPost.getAuthorEmailId();
        this.upVotes = userPost.getUpVotes();
        this.imgUrl = userPost.getImgUrl();
        this.latitude = userPost.getLatLong().getCoordinates()[1];
        this.longitude = userPost.getLatLong().getCoordinates()[0];
        this.creationTimestamp = userPost.getCreationTimestamp();
        this.liked = userPost.isLiked();
        this.authorName = userPost.getAuthorName();
        this.distance = distance;
    }
}
