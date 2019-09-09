package com.tanmesh.splatter.service;

import com.tanmesh.splatter.authentication.UserSession;
import com.tanmesh.splatter.dao.UserDAO;
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
        User user = getUserProfile(emailId);
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
    public void getUserFeed(String emailId) {
//        sanityCheck(emailId, "emailId");
//        User user = userDAO.getUserByEmailId("emailId", emailId);
//        Set<String> userTags = user.getFollowTagList();
//        for (String tag : userTags) {
//            List<UserPost> userPost = userDAO.getAllPost(tag);
//        }
    }

    @Override
    public void followTag(String tag, String emailId) {
        User user = userDAO.getUserByEmailId(emailId);
        Set<String> tagList = user.getFollowTagList();
        if (tagList == null) {
            tagList = new HashSet<>();
        }
        tagList.add(tag);
        user.setFollowTagList(tagList);
        userDAO.save(user);
    }

    @Override
    public void unFollowTag(String tag, String emailId) {
        User user = userDAO.getUserByEmailId(emailId);
        Set<String> tagList = user.getFollowTagList();
        if (tagList == null) {
            return;
        }
        tagList.remove(tag);
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
    public User getUserProfile(String emailId) {
        return userDAO.getUserByEmailId(emailId);
    }

    // TODO: complete getUserFeed
}