package com.tanmesh.spotfood.service;

import com.tanmesh.spotfood.entity.UserPost;
import com.tanmesh.spotfood.exception.InvalidInputException;
import com.tanmesh.spotfood.exception.PostNotFoundException;
import com.tanmesh.spotfood.wsRequestModel.UserPostData;

import java.io.IOException;
import java.util.List;

public interface IUserPostService {
    void addPost(UserPostData userPostData, String emailId) throws Exception;

    void deletePost(String postid) throws InvalidInputException, PostNotFoundException;

    UserPost likePost(String emailId, String postId) throws InvalidInputException, PostNotFoundException;

    void unlikePost(String emailId, String postId) throws InvalidInputException, PostNotFoundException;

    UserPost getPost(String postId) throws InvalidInputException, PostNotFoundException;

    List<UserPostData> getAllPostOfUser(String emailId, int startAfter) throws InvalidInputException;

    List<UserPostData> getAllPost(int startAfter) throws InvalidInputException;

    // TODO: add edit User Post
    boolean editPost(String postId, List<String> tagList, String location, String authorName) throws InvalidInputException;

    void addDummyPost() throws Exception;
}