package com.tanmesh.splatter.service;

import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.InvalidInputException;

import java.util.List;

public interface IUserPostService {
    boolean addPost(String postId, List<String> tagList, String location, String authorName) throws InvalidInputException;
    boolean deletePost(String postId) throws InvalidInputException;
    boolean editPost(String postId, List<String> tagList, String location, String authorName) throws InvalidInputException;
    UserPost likePost(String postId) throws InvalidInputException;
    UserPost getPost(String postId) throws InvalidInputException;
    List<UserPost> getAllPostOfUser(String authorName) throws InvalidInputException;
}
