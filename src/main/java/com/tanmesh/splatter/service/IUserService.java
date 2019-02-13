package com.tanmesh.splatter.service;

import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.UserData;

import java.util.List;

public interface IUserService {
    boolean deleteUser(String emailID) throws InvalidInputException;
    boolean userExists(String emailId);
    List<User> userInfo();
    boolean signUpUser(UserData userData) throws InvalidInputException;
    boolean followTag(String tag, String emailId) throws InvalidInputException;
    boolean unFollowTag(String tag, String emailId) throws InvalidInputException;
    User userProfile(String emailId) throws InvalidInputException;
    boolean logInUser(String emailId, String password) throws InvalidInputException;
}
