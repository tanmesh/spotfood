package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.UserDAO;
import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.UserData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserService implements IUserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<User> userInfo() throws InvalidInputException {
        List<String> userIdList = userDAO.findIds();
        List<User> userList = new ArrayList<>();
        if (userIdList == null || userIdList.size() == 0) {
            throw new InvalidInputException("userIdList is null");
        }
        for (String id : userIdList) {
            userList.add(userDAO.get(id));
        }
        return userList;
    }

    @Override
    public void userExists(String emailId) throws InvalidInputException {
        List<String> userIdList = userDAO.findIds();
        if (userIdList == null || userIdList.size() == 0) {
            throw new InvalidInputException("userIdList is null");
        }
    }

    @Override
    public void signUpUser(UserData userData) throws InvalidInputException {
        if (userData == null) {
            throw new InvalidInputException("UserData is null");
        }
        String firstName = userData.getFirstName();
        String lastName = userData.getLastName();
        String nickName = userData.getNickName();
        String emailId = userData.getEmailId();
        String password = userData.getPassword();

        sanityCheck(firstName, "firstName");
        sanityCheck(lastName, "lastName");
        sanityCheck(nickName, "nickName");
        sanityCheck(emailId, "emailId");
        sanityCheck(password, "password");

        addSignUpUserHelper(firstName, lastName, nickName, emailId, password);
    }

    private void addSignUpUserHelper(String firstName, String lastName, String userNickName, String userEmailId, String userPassword) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setNickName(userNickName);
        user.setEmailId(userEmailId);
        user.setPassword(userPassword);
        updateUser(user);
    }

    // TODO: complete getUserFeed

    @Override
    public void getUserFeed(String emailId) throws InvalidInputException {
//        sanityCheck(emailId, "emailId");
//        User user = userDAO.getUserByEmailId("emailId", emailId);
//        Set<String> userTags = user.getFollowTagList();
//        for (String tag : userTags) {
//            List<UserPost> userPost = userDAO.getAllPost(tag);
//        }
    }

    @Override
    public void followTag(String tag, String emailId) throws InvalidInputException {
        sanityCheck(tag, "tag");
        sanityCheck(emailId, "emailId");
        User user = userDAO.getUserByEmailId(emailId);
        Set<String> tagList = user.getFollowTagList();
        if (tagList == null) {
            tagList = new HashSet<>();
        }
        tagList.add(tag);
        user.setFollowTagList(tagList);
        updateUser(user);
    }

    @Override
    public void unFollowTag(String tag, String emailId) throws InvalidInputException {
        sanityCheck(tag, "tag");
        sanityCheck(emailId, "emailId");
        User user = userDAO.getUserByEmailId(emailId);
        Set<String> tagList = user.getFollowTagList();
        if (tagList == null || tagList.size() == 0) {
            return;
        }
        tagList.remove(tag);
        updateUser(user);
    }

    @Override
    public void deleteUser(String emailId) throws InvalidInputException {
        sanityCheck(emailId, "emailId");
        User user = userDAO.getUserByEmailId(emailId);
        if (user == null) {
            return;
        }
        userDAO.delete(user);
    }

    @Override
    public User getUserProfile(String emailId) throws InvalidInputException {
        sanityCheck(emailId, "emailId");
        return userDAO.getUserByEmailId(emailId);
    }

    private void updateUser(User user) {
        userDAO.save(user);
    }

    private void sanityCheck(String id, String msg) throws InvalidInputException {
        if (id == null || id.length() == 0) {
            throw new InvalidInputException(msg + " is NULL");
        }
    }
}