package com.tanmesh.spotfood.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.HashSet;
import java.util.Set;

@Entity(value = "user", noClassnameStored = true)
public class User {
    @Id
    private String emailId;
    private String firstName;
    private String lastName;
    private String nickName;
    private String password;
    private Set<String> followingList = new HashSet<>();
    private Set<String> followersList = new HashSet<>();
    private Set<Tag> tagList = new HashSet<>();
    private LatLong lastUpdatedLocation;
    private String profilePicUrl;

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public Set<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(Set<Tag> tagList) {
        this.tagList.addAll(tagList);
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

    public LatLong getLastUpdatedLocation() {
        return lastUpdatedLocation;
    }

    public void setLastUpdatedLocation(LatLong lastUpdatedLocation) {
        this.lastUpdatedLocation = lastUpdatedLocation;
    }
}
