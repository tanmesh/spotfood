package com.tanmesh.splatter.service;

import com.tanmesh.splatter.authentication.UserSession;
import com.tanmesh.splatter.dao.UserDAO;
import com.tanmesh.splatter.entity.Tag;
import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.UserData;

import java.util.*;

public class UserService implements IUserService {
    private UserDAO userDAO;
    private AccessTokenService accessTokenService;

    public UserService(UserDAO userDAO, AccessTokenService accessTokenService) {
        this.userDAO = userDAO;
        this.accessTokenService = accessTokenService;
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
    public void signUpUser(UserData userData) {
        User user = new User();
        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        user.setNickName(userData.getNickName());
        user.setEmailId(userData.getEmailId());
        user.setPassword(userData.getPassword());
        userDAO.save(user);
    }

    public UserSession logInUser(String emailId, String inputPassword) throws Exception {
        User user = userDAO.getUserByEmailId(emailId);
        if (user == null) {
            throw new NullPointerException("user not found");
        }
        UserSession userSession;
        try {
            if (!(user.getPassword()).equals(inputPassword)) {
                throw new InvalidInputException("Password is wrong");
            }
            String accessToken = UUID.randomUUID().toString();
            userSession = new UserSession(accessToken, user.getEmailId());
            if (!accessTokenService.saveAccessToken(userSession)) {
                throw new InvalidInputException("Unable to save the new token");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return userSession;
    }

    @Override
    public void followUser(String connectionEmailId, String emailId) throws InvalidInputException {
        User user = userDAO.getUserByEmailId(emailId);
        Set<String> followersList = user.getFollowersList();
        if (followersList == null) {
            followersList = new HashSet<>();
        }
        followersList.add(connectionEmailId);
        user.setFollowersList(followersList);
        userDAO.save(user);
    }

    @Override
    public void unFollowUser(String connectionEmailId, String emailId) throws InvalidInputException {
        User user = userDAO.getUserByEmailId(emailId);
        Set<String> followersList = user.getFollowersList();
        if (followersList == null) {
            return;
        }
        followersList.remove(connectionEmailId);
        user.setFollowersList(followersList);
        userDAO.save(user);
    }

    @Override
    public UserData edit(String emailId, UserData userData) throws Exception {
        User user = userDAO.getUserByEmailId(emailId);

        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        user.setNickName(userData.getNickName());
        user.setTagList(userData.getTagList());
        userDAO.save(user);

        return userData;
    }

    @Override
    public void followTag(Set<String> tags, String emailId) {
        User user = userDAO.getUserByEmailId(emailId);
        Set<Tag> tagList = user.getTagList();
        if (tagList == null) {
            tagList = new HashSet<>();
        }
        for (String tag : tags) {
            tagList.add(new Tag(tag));
        }
        userDAO.save(user);
    }

    @Override
    public void unFollowTag(Set<String> tags, String emailId) {
        User user = userDAO.getUserByEmailId(emailId);
        Set<Tag> tagList = user.getTagList();
        if (tagList == null) {
            return;
        }
        for (String tag : tags) {
            tagList.remove(new Tag(tag));
        }
        userDAO.save(user);
    }

    @Override
    public void deleteUser(String emailId) {
        User user = userDAO.getUserByEmailId(emailId);
        if (user == null) {
            return;
        }
        userDAO.delete(user);
    }

    @Override
    public UserData getUserProfile(String emailId) {
        User user = userDAO.getUserByEmailId(emailId);

        UserData userData = new UserData(user);

        return userData;
    }
}