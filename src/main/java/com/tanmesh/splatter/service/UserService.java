package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.UserDAO;
import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.UserData;
import org.mongodb.morphia.Key;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean addUser(UserData userData) throws InvalidInputException {
        if (userData == null) {
            throw new InvalidInputException("UserData is null");
        }
        String firstName = userData.getFirstName();
        String lastName = userData.getLastName();
        int userID = userData.getUserID();
        if (firstName == null || firstName.length() == 0) {
            throw new InvalidInputException("hello");
        }
        if (userExists(userData.getUserID())) {
            return false;
        }
        return addUserHelper(firstName, lastName, userID);
    }

    private boolean addUserHelper(String firstName, String lastName, int userID) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserID(userID);
        Key<User> userKey = userDAO.save(user);
        if (userKey == null) {
            return false;
        }
        return true;
    }

    public boolean removeUser(String firstName, String lastName) throws InvalidInputException {
        if (firstName == null || firstName.length() == 0) {
            throw new InvalidInputException("die");
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
}
