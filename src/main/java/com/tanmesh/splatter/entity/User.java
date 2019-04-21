package com.tanmesh.splatter.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.HashSet;
import java.util.Set;

@Entity(value = "user_data", noClassnameStored = true)
public class User {
    private String firstName;
    private String lastName;
    @Id
    private String emailId;
    private String password;
    private Set<String> followTagList;

    public User() {
        followTagList = new HashSet<>();
    }

    public Set<String> getFollowTagList() {
        return followTagList;
    }

    public boolean followTag(String tag) {
        if (!followTagList.contains(tag)) {
            followTagList.add(tag);
            return true;
        }
        return false;
    }

    public boolean unfollowTag(String tag) {
        if (followTagList.contains(tag)) {
            followTagList.remove(tag);
            return true;
        }
        return false;
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
}
