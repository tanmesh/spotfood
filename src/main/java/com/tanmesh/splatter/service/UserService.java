package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.UserDAO;
import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.UserData;
import org.mongodb.morphia.Key;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UserService {
    private UserDAO userDAO;
    private HashMap<String, String> userToken;

    public UserService(UserDAO userDAO, HashMap<String, String> userToken) {
        this.userDAO = userDAO;
        this.userToken = userToken;
    }

    public boolean removeUser(String firstName) throws InvalidInputException {
        if (firstName == null || firstName.length() == 0) {
            throw new InvalidInputException("first name is null");
        }
        return false;
    }

    public boolean userExists(int userID) {
        List<Integer> userIdList = userDAO.findIds();
        if (userIdList == null || userIdList.size() == 0) {
            return false;
        }
        return userIdList.contains(userID);
    }

    public List<User> userInfo() {
        List<Integer> userIdList = userDAO.findIds();
        List<User> userList = new ArrayList<>();
        if (userIdList == null || userIdList.size() == 0) {
            return userList;
        }
        for (int id : userIdList) {
            userList.add(userDAO.get(id));
        }
        return userList;
    }

    public boolean signUpUser(UserData userData) throws InvalidInputException {
        if (userData == null) {
            throw new InvalidInputException("UserData is null");
        }
        String firstName = userData.getFirstName();
        String lastName = userData.getLastName();
        int userID = userData.getUserID();
        String userNickName = userData.getNickName();
        String userEmailId = userData.getEmailId();
        String userPassword = userData.getPassword();
        if (firstName == null || firstName.length() == 0) {
            throw new InvalidInputException("first name is null");
        }
        if (userExists(userData.getUserID())) {
            return false;
        }
        return addSignUpUserHelper(firstName, lastName, userID, userNickName, userEmailId, userPassword);
    }

    private boolean addSignUpUserHelper(String firstName, String lastName, int userID, String userNickName, String userEmailId, String userPassword) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserID(userID);
        user.setNickName(userNickName);
        user.setEmailId(userEmailId);
        user.setPassword(userPassword);
        Key<User> userKey = userDAO.save(user);
        return userKey != null;
    }

    public String logInUser(UserData userData) throws InvalidInputException {
        if(userData == null) {
            throw new InvalidInputException("UserData is null");
        }
        String enteredEmailId = userData.getEmailId();
        String enteredPassword = userData.getPassword();
        if(enteredEmailId == null || enteredEmailId.length() == 0) {
            throw new InvalidInputException("empty email id");
        }
        if(enteredPassword == null || enteredPassword.length() == 0) {
            throw new InvalidInputException("enteredPassword is empty");
        }

        User user = userDAO.getDatastore().createQuery(User.class).filter("emailId", enteredEmailId).filter("password", enteredPassword).get();

        String token = UUID.randomUUID().toString();
        userToken.put(enteredEmailId, token);

        return token;
    }

    public String followUser(UserData userData) throws InvalidInputException {
        if(userData == null) {
            throw new InvalidInputException("UserData is null");
        }
        return "Wow!";
    }
}