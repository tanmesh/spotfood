package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.UserPostDAO;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.UserPostData;
import org.mongodb.morphia.Key;

import java.util.ArrayList;
import java.util.List;

public class UserPostService implements IUserPostService {
    private UserPostDAO userPostDAO;

    public UserPostService(UserPostDAO userPostDAO) {
        this.userPostDAO = userPostDAO;
    }

    @Override
    public boolean addPost(String postId, List<String> tagList, String location, String authorName) throws InvalidInputException {
        sanityCheck(postId, "postId is NULL");
        sanityCheck(location, "location is NULL");
        sanityCheck(authorName, "authorName is NULL");
        if(tagList == null || tagList.size() == 0) {
            throw new InvalidInputException("tagList is NULL");
        }
        UserPost userPost = new UserPost();
        userPost.setPostId(postId);
        userPost.setLocation(location);
        userPost.setAuthorName(authorName);
        List<String> postTags = userPost.getTags();
        if(postTags == null) {
            postTags = new ArrayList<>();
        }
        postTags.addAll(tagList);
        userPost.setTags(postTags);
        Key<UserPost> userPostKey = userPostDAO.save(userPost);
        return userPostKey != null;
    }

    private void sanityCheck(String postId, String message) throws InvalidInputException {
        if(postId == null || postId.length() == 0) {
            throw new InvalidInputException(message);
        }
    }

    @Override
    public boolean deletePost(String tagId) {

        return false;
    }

    @Override
    public boolean editPost(UserPostData userPostData) {
        return false;
    }

    @Override
    public boolean likePost(UserPostData userPostData) {
        return false;
    }
}
