package com.tanmesh.splatter.service;

import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.UserData;

import java.util.List;

public interface IUserService {
    void deleteUser(String emailID) throws InvalidInputException;

    void userExists(String emailId) throws InvalidInputException;

    List<User> userInfo() throws InvalidInputException;

    void signUpUser(UserData userData) throws InvalidInputException;

    void followTag(String tag, String emailId) throws InvalidInputException;

    void unFollowTag(String tag, String emailId) throws InvalidInputException;

    User getUserProfile(String emailId) throws InvalidInputException;

    void getUserFeed(String emailId) throws InvalidInputException;
}
