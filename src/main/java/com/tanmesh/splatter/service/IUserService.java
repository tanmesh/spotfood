package com.tanmesh.splatter.service;

import com.tanmesh.splatter.authentication.UserSession;
import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.UserData;

import java.util.List;
import java.util.Set;

public interface IUserService {
    void deleteUser(String emailID) throws InvalidInputException;

    void userExists(String emailId) throws InvalidInputException;

    List<User> userInfo() throws InvalidInputException;

    void followTag(Set<String> tag, String emailId) throws InvalidInputException;

    void unFollowTag(Set<String> tag, String emailId) throws InvalidInputException;

    UserData getUserProfile(String emailId);

    void signUpUser(UserData userData);

    UserSession logInUser(String emailId, String password) throws Exception;

    void followUser(UserData userData, String emailId) throws InvalidInputException;

    void unFollowUser(UserData userData, String emailId) throws InvalidInputException;

    UserData edit(String emailId, UserData userData) throws Exception;
}
