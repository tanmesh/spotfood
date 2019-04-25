package com.tanmesh.splatter.service;

import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.exception.PostNotFoundException;
import com.tanmesh.splatter.wsRequestModel.UserPostData;

import java.io.IOException;
import java.util.List;

public interface IUserPostService {
    void addPost(String userId, UserPostData userPostData) throws InvalidInputException, IOException;
    void deletePost(String postId) throws InvalidInputException;
    UserPost likePost(String postId) throws InvalidInputException, PostNotFoundException;
    UserPost getPost(String postId) throws InvalidInputException;
    List<UserPost> getAllPostOfUser(String userEmailId) throws InvalidInputException;
}
