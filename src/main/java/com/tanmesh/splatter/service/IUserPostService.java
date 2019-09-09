package com.tanmesh.splatter.service;

import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.exception.PostNotFoundException;
import com.tanmesh.splatter.wsRequestModel.UserPostData;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IUserPostService {
    void addPost(UserPostData userPostData, String emailId) throws InvalidInputException, IOException;

    void deletePost(String emailId) throws InvalidInputException;

    UserPost likePost(String emailId) throws InvalidInputException, PostNotFoundException;

    UserPost getPost(String emailId) throws InvalidInputException;

    List<UserPost> getAllPostOfUser(String emailId) throws InvalidInputException;

    boolean editPost(String postId, List<String> tagList, String location, String authorName) throws InvalidInputException;

    Set<UserPost> getUserFeed(String emailId);
}