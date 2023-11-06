package com.tanmesh.splatter.wsRequestModel;

import com.tanmesh.splatter.entity.Tag;
import com.tanmesh.splatter.entity.User;

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
    private Double latitude;
    private Double longitude;

    public UserData() {
    }

    public UserData(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.nickName = user.getNickName();
        this.emailId = user.getEmailId();
        this.password = user.getPassword();

        if (user.getTagList() != null) {
            for (Tag tag : user.getTagList()) {
                this.tagList.add(tag.getName());
            }
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
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
}
