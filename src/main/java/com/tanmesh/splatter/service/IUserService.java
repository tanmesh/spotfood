package com.tanmesh.splatter.service;

import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.EmailIdAlreadyRegistered;
import com.tanmesh.splatter.exception.EmailIdNotRegistered;
import com.tanmesh.splatter.exception.IncorrectPassword;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.UserData;

import java.util.List;

public interface IUserService {
    // new user sign up
    User signUpUser(UserData userData) throws InvalidInputException, EmailIdAlreadyRegistered;

    // login for existing user
    User logInUser(String emailId, String password) throws InvalidInputException, EmailIdNotRegistered, IncorrectPassword;

    void deleteUser(String emailID) throws InvalidInputException;
    void userExists(String emailId) throws InvalidInputException;
    List<User> userInfo() throws InvalidInputException;

    void followTag(String tag, String emailId) throws InvalidInputException;
    void unFollowTag(String tag, String emailId) throws InvalidInputException;
    User userProfile(String emailId) throws InvalidInputException;

    void getUserFeed(String emailId) throws InvalidInputException;
}
