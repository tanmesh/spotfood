package com.tanmesh.splatter.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.HashSet;
import java.util.Set;

@Entity(value = "user_data", noClassnameStored = true)
public class User {
    private String firstName;
    private String lastName;
    private String nickName;
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

    public void setFollowTagList(Set<String> followTagList) {
        this.followTagList = followTagList;
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
