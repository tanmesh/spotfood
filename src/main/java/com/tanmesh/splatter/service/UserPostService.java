package com.tanmesh.splatter.service;

import com.mongodb.WriteResult;
import com.tanmesh.splatter.dao.UserPostDAO;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.InvalidInputException;
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
        if (tagList == null || tagList.size() == 0) {
            throw new InvalidInputException("tagList is NULL");
        }
        UserPost userPost = new UserPost();
        userPost.setPostId(postId);
        userPost.setLocation(location);
        userPost.setAuthorName(authorName);
        List<String> postTags = userPost.getTags();
        if (postTags == null) {
            postTags = new ArrayList<>();
        }
        postTags.addAll(tagList);
        userPost.setTags(postTags);
        Key<UserPost> userPostKey = userPostDAO.save(userPost);
        return userPostKey != null;
    }

    @Override
    public boolean editPost(String postId, List<String> tagList, String location, String authorName) throws InvalidInputException {
        return true;
    }

    @Override
    public boolean deletePost(String postId) throws InvalidInputException {
        sanityCheck(postId, "postId is NULL");
        UserPost userPost = userPostDAO.getPost("postId", postId);
        userPostDAO.delete(userPost);
        return userPost != null;
    }

    @Override
    public UserPost likePost(String postId) throws InvalidInputException {
        sanityCheck(postId, "postId is NULL");
        UserPost userPost = userPostDAO.getPost("postId", postId);
        int prevCnt = userPost.getUpVotes();
        int updatedCnt = prevCnt + 1;
        userPost.setUpVotes(updatedCnt);
        Key<UserPost> key = userPostDAO.save(userPost);
        if (key == null) {
            System.out.println("couldn't do");
        }
        return userPost;
    }

    @Override
    public UserPost getPost(String postId) throws InvalidInputException {
        sanityCheck(postId, "postId is NULL");

        return userPostDAO.getPost("postId", postId);
    }

    @Override
    public List<UserPost> getAllPostOfUser(String authorName) throws InvalidInputException {
        sanityCheck(authorName, "authorName is NULL");
        return userPostDAO.getAllPost(authorName);
    }

//    public void savePost(String imageInHex) throws InvalidInputException {
//    }

    private void sanityCheck(String postId, String message) throws InvalidInputException {
        if (postId == null || postId.length() == 0) {
            throw new InvalidInputException(message);
        }
    }
}
