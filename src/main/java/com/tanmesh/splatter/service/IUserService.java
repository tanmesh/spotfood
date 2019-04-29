package com.tanmesh.splatter.service;

import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.EmailIdAlreadyRegistered;
import com.tanmesh.splatter.exception.EmailIdNotRegistered;
import com.tanmesh.splatter.exception.IncorrectPassword;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.UserData;
import com.tanmesh.splatter.wsResponseModel.UserProfileResponse;

import java.util.List;
import java.util.Set;

public interface IUserService {
    // new user sign up
    void signUpUser(UserData userData) throws InvalidInputException, EmailIdAlreadyRegistered;

    // login for existing user
    User logInUser(String emailId, String password) throws InvalidInputException, EmailIdNotRegistered, IncorrectPassword;

    // logout for logged-in user
    void logOutUser(String emailId, String accessToken) throws InvalidInputException;

    // user service for following a tag
    void followTag(String emailId, String tag) throws InvalidInputException;

    // user service for un-following the followed tag
    void unFollowTag(String emailId, String tag) throws InvalidInputException;

    // user service to get the list of all following tags
    Set<String> getFollowingTags(String emailId);

    // user service to get the count of all following tags
    int getFollowingTagCount(String emailId);

    // user service to follow another user
    void followUser(String follower, String following) throws InvalidInputException;

    // user service to follow another user
    void unfollowUser(String follower, String following) throws InvalidInputException;

    // user service to get the list of all following users
    Set<String> getFollowingUsers(String emailId);

    void deleteUser(String emailID) throws InvalidInputException;
    void userExists(String emailId) throws InvalidInputException;
    List<User> userInfo() throws InvalidInputException;

    // get User Class
    User getUser(String emailId);

    // get complete user profile (User class info and user posts
    UserProfileResponse getUserProfile(String emailId) throws InvalidInputException;

    Set<UserPost> getUserFeed(String emailId) throws InvalidInputException;
}
