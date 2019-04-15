package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.UserDAO;
import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.EmailIdAlreadyRegistered;
import com.tanmesh.splatter.exception.EmailIdNotRegistered;
import com.tanmesh.splatter.exception.IncorrectPassword;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.utils.FormatUtils;
import com.tanmesh.splatter.wsRequestModel.UserData;

import java.util.*;

public class UserService implements IUserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

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

    public void userExists(String emailId) throws InvalidInputException {
        List<String> userIdList = userDAO.findIds();
        if (userIdList == null || userIdList.size() == 0) {
            throw new InvalidInputException("userIdList is null");
        }
    }

    public void signUpUser(UserData userData) throws InvalidInputException, EmailIdAlreadyRegistered {
        if (userData == null) {
            throw new InvalidInputException("UserData is null");
        }

        String firstName = userData.getFirstName();
        String lastName = userData.getLastName();
        String emailId = userData.getEmailId();
        String password = userData.getPassword();

        sanityCheck(firstName, "firstName");
        sanityCheck(lastName, "lastName");
        sanityCheck(emailId, "emailId");
        sanityCheck(password, "password");


        if (userDAO.userAlreadyExists(emailId)) {
            String errorMsg = FormatUtils.format("emailId:{0} already registered, please login", emailId);
            throw new EmailIdAlreadyRegistered(errorMsg);
        }

        addNewUser(firstName, lastName, emailId, password);
    }

    private void addNewUser(String firstName, String lastName, String userEmailId, String userPassword) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmailId(userEmailId);
        user.setPassword(userPassword);
        userDAO.save(user);
    }

    public User logInUser(String emailId, String password) throws InvalidInputException,EmailIdNotRegistered, IncorrectPassword {
        sanityCheck(password, "password");
        sanityCheck(emailId, "emailId");

        User user = userDAO.getUserFromEmailId(emailId);
        if (user == null) {
            String errorMsg = FormatUtils.format("emailId:{0} not registered, please signup", emailId);
            throw new EmailIdNotRegistered(errorMsg);
        }

        if (!user.getPassword().equals(password)) {
            String errorMsg = FormatUtils.format("password is incorrect for emailId:{0} ", emailId);
            throw new IncorrectPassword(errorMsg);
        }

        AuthService.addNewAccessToken(user);
        return user;
    }

    // TODO: complete getUserFeed
    @Override
    public void getUserFeed(String emailId) throws InvalidInputException {
//        sanityCheck(emailId, "emailId");
//        User user = userDAO.getUser("emailId", emailId);
//        Set<String> userTags = user.getFollowTagList();
//        for (String tag : userTags) {
//            List<UserPost> userPost = userDAO.getAllPost(tag);
//        }
    }

    public void followTag(String tag, String emailId) throws InvalidInputException {
        sanityCheck(tag, "tag");
        sanityCheck(emailId, "emailId");
        User user = userDAO.getUser("emailId", emailId);
        Set<String> tagList = user.getFollowTagList();
        if (tagList == null) {
            tagList = new HashSet<>();
        }
        tagList.add(tag);
        user.setFollowTagList(tagList);
        userDAO.save(user);
    }

    public void unFollowTag(String tag, String emailId) throws InvalidInputException {
        sanityCheck(tag, "tag");
        sanityCheck(emailId, "emailId");
        User user = userDAO.getUser("emailId", emailId);
        Set<String> tagList = user.getFollowTagList();
        if (tagList == null || tagList.size() == 0) {
            return;
        }
        tagList.remove(tag);
        userDAO.save(user);
    }

    @Override
    public void deleteUser(String emailId) throws InvalidInputException {
        sanityCheck(emailId, "emailId");
        User user = userDAO.getUser("emailId", emailId);
        if (user == null) {
            return;
        }
        userDAO.delete(user);
    }

    @Override
    public User userProfile(String emailId) throws InvalidInputException {
        sanityCheck(emailId, "emailId");
        return userDAO.getUser("emailId", emailId);
    }

    private void sanityCheck(String id, String msg) throws InvalidInputException {
        if (id == null || id.length() == 0) {
            throw new InvalidInputException(msg + " is NULL");
        }
    }
}