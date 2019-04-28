package com.tanmesh.splatter.service;

import com.tanmesh.splatter.authentication.AuthService;
import com.tanmesh.splatter.dao.UserDAO;
import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.EmailIdAlreadyRegistered;
import com.tanmesh.splatter.exception.EmailIdNotRegistered;
import com.tanmesh.splatter.exception.IncorrectPassword;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.utils.FormatUtils;
import com.tanmesh.splatter.wsRequestModel.UserData;
import com.tanmesh.splatter.wsResponseModel.UserProfileResponse;

import java.util.*;

public class UserService implements IUserService {
    private UserDAO userDAO;
    private AuthService authService;
    private IUserPostService userPostService;

    public UserService(UserDAO userDAO, AuthService authService, IUserPostService userPostService) {
        this.userDAO = userDAO;
        this.authService = authService;
        this.userPostService = userPostService;
    }

    private void sanityCheck(String id, String msg) throws InvalidInputException {
        if (id == null || id.length() == 0) {
            throw new InvalidInputException(msg + " is NULL");
        }
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

        authService.addNewAccessToken(user);
        return user;
    }

    public void logOutUser(String emailId, String accessToken) throws InvalidInputException {
        if (authService.removeAccessToken(emailId, accessToken) == false) {
            String errorMsg = FormatUtils.format("emailId:{0} not valid", emailId);
            throw new InvalidInputException(errorMsg);
        }
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


    public void followTag(String emailId, String tag) throws InvalidInputException {
        sanityCheck(emailId, "emailId");
        sanityCheck(tag, "tag");

        User user = userDAO.getUser("emailId", emailId);
        if (user.followTag(tag)) {
            userDAO.save(user);
        } else {
            String errorMsg = FormatUtils.format("emailId:{0} is already following tag:{1}", emailId, tag);
            throw new InvalidInputException(errorMsg);
        }
    }

    public void unFollowTag(String emailId, String tag) throws InvalidInputException {
        sanityCheck(emailId, "emailId");
        sanityCheck(tag, "tag");

        User user = userDAO.getUser("emailId", emailId);
        if (user.unfollowTag(tag)) {
            userDAO.save(user);
        } else {
            String errorMsg = FormatUtils.format("emailId:{0} is not following tag:{1}", emailId, tag);
            throw new InvalidInputException(errorMsg);
        }
    }

    public Set<String> getFollowingTags(String emailId) {
        User user = userDAO.getUser("emailId", emailId);
        return user.getFollowTagList();
    }

    public int getFollowingTagCount(String emailId) {
        User user = userDAO.getUser("emailId", emailId);
        return user.getFollowingTagCount();
    }

    public void followUser(String follower, String following) throws InvalidInputException {
        sanityCheck(follower, "followerId");
        sanityCheck(following, "followingId");

        User user = userDAO.getUser("emailId", follower);
        if (user.followUser(following)) {
            userDAO.save(user);
        } else {
            String errorMsg = FormatUtils.format("emailId:{0} is already following user:{1}", follower, following);
            throw new InvalidInputException(errorMsg);
        }
    }

    public void unfollowUser(String follower, String following) throws InvalidInputException{
        sanityCheck(follower, "followerId");
        sanityCheck(following, "followingId");

        User user = userDAO.getUser("emailId", follower);
        if (user.unfollowUser(following)) {
            userDAO.save(user);
        } else {
            String errorMsg = FormatUtils.format("emailId:{0} is not following user:{1}", follower, following);
            throw new InvalidInputException(errorMsg);
        }
    }

    public Set<String> getFollowingUsers(String emailId) {
        User user = userDAO.getUser("emailId", emailId);
        return user.getFollowingUserList();
    }

    @Override
    public UserProfileResponse getUserProfile(String emailId) throws InvalidInputException {
        sanityCheck(emailId, "emailId");
        User user = userDAO.getUser("emailId", emailId);
        List<UserPost> userPosts = userPostService.getAllPostOfUser(emailId);
        UserProfileResponse userProfileResponse = new UserProfileResponse(user, userPosts);
        return userProfileResponse;
    }

    @Override
    public Set<UserPost> getUserFeed(String emailId) throws InvalidInputException {
        sanityCheck(emailId, "emailId");

        Set<UserPost> feeds = new HashSet<>();
        User user = userDAO.getUser("emailId", emailId);

        // add all the posts containing user's following tags
        Set<String> userTags = user.getFollowTagList();
        for (String tag : userTags) {
            List<UserPost> userPosts = userDAO.getAllPosts(tag);
            for (UserPost userPost : userPosts) {
                feeds.add(userPost);
            }
        }

        // add all the posts added by user's following users
        Set<String> followingUserIds = user.getFollowingUserList();
        for (String followingUserId : followingUserIds) {
            List<UserPost> userPosts = userPostService.getAllPostOfUser(followingUserId);
            for (UserPost userPost : userPosts) {
                feeds.add(userPost);
            }
        }


        // add a timestamp field in UserPost so that feeds can be sorted based on the post timestamp
        // for now adding all the feeds to the user post
        // limit the number of feeds with the timestamp

        return feeds;
    }
}