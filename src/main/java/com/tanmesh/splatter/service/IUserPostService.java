package com.tanmesh.splatter.service;

import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.exception.PostNotFoundException;
import com.tanmesh.splatter.wsRequestModel.UserPostData;

import java.io.IOException;
import java.util.List;

public interface IUserPostService {
    void addPost(UserPostData userPostData, String emailId) throws InvalidInputException, IOException;

    void deletePost(String postid) throws InvalidInputException, PostNotFoundException;

    UserPost likePost(String emailId, String postId) throws InvalidInputException, PostNotFoundException;

    void unlikePost(String emailId, String postId) throws InvalidInputException, PostNotFoundException;

    UserPost getPost(String postId) throws InvalidInputException, PostNotFoundException;

    List<UserPostData> getAllPostOfUser(String emailId, int startAfter) throws InvalidInputException;

    boolean editPost(String postId, List<String> tagList, String location, String authorName) throws InvalidInputException;

    List<UserPostData> getUserFeed(String emailId, int startAfter);

    List<UserPostData> getUserExplore(int startAfter, String emailId);

    void addDummyPost() throws InvalidInputException, IOException;
}