package com.tanmesh.spotfood.service;

import com.tanmesh.spotfood.authentication.UserSession;
import com.tanmesh.spotfood.entity.User;
import com.tanmesh.spotfood.exception.InvalidInputException;
import com.tanmesh.spotfood.wsRequestModel.UserData;

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
