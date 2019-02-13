package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.UserDAO;
import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.UserData;
import org.mongodb.morphia.Key;

import java.util.*;

public class UserService implements IUserService {
    private UserDAO userDAO;
    private HashMap<String, String> userToken;

    public UserService(UserDAO userDAO, HashMap<String, String> userToken) {
        this.userDAO = userDAO;
        this.userToken = userToken;
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
        sanityCheck(firstName, "firstName is NULL");
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
        return updateUser(user);
    }

    public String logInUser(UserData userData) throws InvalidInputException {
        if (userData == null) {
            throw new InvalidInputException("UserData is null");
        }
        String enteredEmailId = userData.getEmailId();
        String enteredPassword = userData.getPassword();
        sanityCheck(enteredEmailId, "emailId is NULL");
        sanityCheck(enteredPassword, "emailId is NULL");

        User user = userDAO.getDatastore().createQuery(User.class).filter("emailId", enteredEmailId).filter("password", enteredPassword).get();

        String token = UUID.randomUUID().toString();
        userToken.put(user.getEmailId(), token);

        return token;
    }

    //TODO: change the ID to emailId
    public boolean followTag(String tag, String emailId) throws InvalidInputException {
        sanityCheckOfEmailIdAndTag(tag, emailId);

        User user = userDAO.getDatastore().createQuery(User.class).filter("emailId", emailId).get();

        Set<String> tagList = user.getFollowTagList();
        if (tagList == null) {
            tagList = new HashSet<>();
        }
        tagList.add(tag);
        user.setFollowTagList(tagList);
        return updateUser(user);
    }

    public boolean unFollowTag(String tag, String emailId) throws InvalidInputException {
        sanityCheckOfEmailIdAndTag(tag, emailId);

        User user = userDAO.getDatastore().createQuery(User.class).filter("emailId", emailId).get();

        Set<String> tagList = user.getFollowTagList();
        if (tagList == null || tagList.size() == 0) {
            return false;
        }
        tagList.remove(tag);
        return updateUser(user);
    }

    private boolean updateUser(User user) {
        Key<User> userKey = userDAO.save(user);
        return userKey != null;
    }

    private void sanityCheckOfEmailIdAndTag(String tag, String emailId) throws InvalidInputException {
        if (tag == null) {
            throw new InvalidInputException("followTagList is NULL");
        }
        sanityCheck(emailId, "emailId is NULL");
    }

    @Override
    public boolean deleteUser(String emailId) throws InvalidInputException {
        sanityCheck(emailId, "emailId is NULL");

        User user = userDAO.getDatastore().createQuery(User.class).filter("emailId", emailId).get();
        if(user == null) {
            return false;
        }
        userDAO.delete(user);
        return true;
    }

    public User userProfile(String emailId) throws InvalidInputException{
        sanityCheck(emailId, "emailId is NULL");

        User user = userDAO.getDatastore().createQuery(User.class).filter("emailId", emailId).get();
        return user;
    }

    private void sanityCheck(String emailId, String s) throws InvalidInputException {
        if (emailId == null || emailId.length() == 0) {
            throw new InvalidInputException(s);
        }
    }
}