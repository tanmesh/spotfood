package com.tanmesh.splatter.service;

import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.EmailIdAlreadyRegistered;
import com.tanmesh.splatter.exception.EmailIdNotRegistered;
import com.tanmesh.splatter.exception.IncorrectPassword;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.UserData;

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

    void deleteUser(String emailID) throws InvalidInputException;
    void userExists(String emailId) throws InvalidInputException;
    List<User> userInfo() throws InvalidInputException;


    User getUserProfile(String emailId) throws InvalidInputException;

    Set<UserPost> getUserFeed(String emailId) throws InvalidInputException;
}
