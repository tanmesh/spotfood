package com.tanmesh.spotfood.wsRequestModel;

import com.tanmesh.spotfood.entity.Tag;
import com.tanmesh.spotfood.entity.User;

import java.util.HashSet;
import java.util.Set;

public class UserData {
    private String emailId;
    private String firstName;
    private String lastName;
    private String nickName;
    private String password;
    private Set<String> followingList = new HashSet<>();
    private Set<String> followersList = new HashSet<>();
    private Set<String> tagList = new HashSet<>();
    private String profilePicUrl;

    public UserData() {
    }

    public UserData(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.nickName = user.getNickName();
        this.emailId = user.getEmailId();
        this.password = user.getPassword();
        this.followersList = user.getFollowersList();
        this.followingList = user.getFollowingList();
        this.profilePicUrl = user.getProfilePicUrl();

        if (user.getTagList() != null) {
            for (Tag tag : user.getTagList()) {
                this.tagList.add(tag.getName());
            }
        }
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(Set<String> followingList) {
        this.followingList = followingList;
    }

    public Set<String> getFollowersList() {
        return followersList;
    }

    public void setFollowersList(Set<String> followersList) {
        this.followersList = followersList;
    }

    public Set<String> getTagList() {
        return tagList;
    }

    public void setTagList(Set<String> tagList) {
        this.tagList = tagList;
    }
}
