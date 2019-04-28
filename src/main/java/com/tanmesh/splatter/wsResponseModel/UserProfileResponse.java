package com.tanmesh.splatter.wsResponseModel;

import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.entity.UserPost;

import java.util.List;

public class UserProfileResponse {
    private User user;
    private List<UserPost> userPostList;

    public UserProfileResponse() {
    }

    public UserProfileResponse(User user, List<UserPost> userPostList) {
        this.user = user;
        this.userPostList = userPostList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserPost> getUserPostList() {
        return userPostList;
    }

    public void setUserPostList(List<UserPost> userPostList) {
        this.userPostList = userPostList;
    }

    public int getUserPostCount() {
        return userPostList.size();
    }
}
