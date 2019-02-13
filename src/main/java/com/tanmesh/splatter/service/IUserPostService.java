package com.tanmesh.splatter.service;

import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.UserPostData;

import java.util.List;

public interface IUserPostService {
    boolean addPost(String postId, List<String> tagList, String location, String authorName) throws InvalidInputException;
    boolean deletePost(String tagId) throws InvalidInputException;
    boolean editPost(UserPostData userPostData) throws InvalidInputException;
    boolean likePost(UserPostData userPostData) throws InvalidInputException;
    boolean getPost(UserPostData userPostData) throws InvalidInputException;
    boolean getAllPostOfUser(UserPostData userPostData) throws InvalidInputException;
}
