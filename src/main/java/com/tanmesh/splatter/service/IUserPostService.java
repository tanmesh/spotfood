package com.tanmesh.splatter.service;

import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.exception.PostNotFoundException;

import java.io.IOException;
import java.util.List;

public interface IUserPostService {
    void addPost(String postId, List<String> tagList, String location, String authorName, String encodedImage) throws InvalidInputException, IOException;

    void deletePost(String postId) throws InvalidInputException;

    UserPost likePost(String postId) throws InvalidInputException, PostNotFoundException;

    UserPost getPost(String postId) throws InvalidInputException;

    List<UserPost> getAllPostOfUser(String emailId) throws InvalidInputException;

    boolean editPost(String postId, List<String> tagList, String location, String authorName) throws InvalidInputException;
}
