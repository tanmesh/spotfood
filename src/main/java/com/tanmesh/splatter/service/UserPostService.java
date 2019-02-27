package com.tanmesh.splatter.service;

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
        sanityCheck(postId, "postId");
        sanityCheck(location, "location");
        sanityCheck(authorName, "authorName");
        if (tagList == null || tagList.size() == 0) {
            throw new InvalidInputException("tagList");
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
    public boolean editPost(String postId, List<String> tagList, String location, String authorName) {
        return true;
    }

    @Override
    public boolean deletePost(String postId) throws InvalidInputException {
        sanityCheck(postId, "postId");
        UserPost userPost = userPostDAO.getPost("postId", postId);
        userPostDAO.delete(userPost);
        return userPost != null;
    }

    @Override
    public UserPost likePost(String postId) throws InvalidInputException {
        sanityCheck(postId, "postId");
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
        sanityCheck(postId, "postId");
        return userPostDAO.getPost("postId", postId);
    }

    @Override
    public List<UserPost> getAllPostOfUser(String authorName) throws InvalidInputException {
        sanityCheck(authorName, "authorName");
        return userPostDAO.getAllPost("authorName", authorName);
    }

//    public void savePost(String imageInHex) throws InvalidInputException {
//    }

    private void sanityCheck(String postId, String message) throws InvalidInputException {
        if (postId == null || postId.length() == 0) {
            throw new InvalidInputException(message+" is NULL");
        }
    }
}
