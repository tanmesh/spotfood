package com.tanmesh.splatter.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import scala.util.parsing.combinator.testing.Str;

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
    private Set<String> followUserList;

    public User() {
        followTagList = new HashSet<>();
        followUserList = new HashSet<>();
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

    public Set<String> getFollowTagList() {
        return followTagList;
    }

    public int getFollowingTagCount() {
        return followTagList.size();
    }

    public Set<String> getFollowingUserList() {
        return followUserList;
    }

    public int getFollowingUserCount() {
        return followUserList.size();
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

    public boolean isFollowingTag(Tag tag) {
        if (followTagList.contains(tag.getName())) {
            return true;
        }
        return false;
    }

    public boolean followUser(String userId) {
        if (!followUserList.contains(userId)) {
            followUserList.add(userId);
            return true;
        }
        return false;
    }

    public boolean unfollowUser(String userId) {
        if (followUserList.contains(userId)) {
            followUserList.remove(userId);
            return true;
        }
        return false;
    }
}
