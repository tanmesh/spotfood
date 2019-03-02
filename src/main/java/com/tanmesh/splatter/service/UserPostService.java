package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.UserPostDAO;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.exception.PostNotFoundException;
import org.apache.commons.io.FileUtils;
import org.mongodb.morphia.Key;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserPostService implements IUserPostService {
    private UserPostDAO userPostDAO;

    public UserPostService(UserPostDAO userPostDAO) {
        this.userPostDAO = userPostDAO;
    }

    @Override
    public boolean addPost(String postId, List<String> tagList, String location, String authorName, String encodedImg) throws InvalidInputException, IOException {
        sanityCheck(postId, "postId");
        sanityCheck(location, "location");
        sanityCheck(authorName, "authorName");
        if (tagList == null || tagList.size() == 0) {
            throw new InvalidInputException("tagList");
        }
        if (encodedImg == null) {
            throw new InvalidInputException("encodedImg");
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
        String filePath = "/Users/tanmesh/Downloads/2019-02-10.jpg";
        byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
        encodedImg = Base64.getEncoder().encodeToString(fileContent);
        userPost.setEncodedImg(encodedImg);
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
    public UserPost likePost(String postId) throws InvalidInputException, PostNotFoundException {
        sanityCheck(postId, "postId");
        UserPost userPost = userPostDAO.getPost("postId", postId);
        if(userPost == null) {
            throw new PostNotFoundException("user post is NULL");
        }
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
        return userPostDAO.getAllPostOfUser("authorName", authorName);
    }

//    @Override
//    public List<UserPost> getAllPostOfUser() {
//        return userPostDAO.getAllPost();
//    }

//    public void savePost(String imageInHex) throws InvalidInputException {
//    }

    private void sanityCheck(String postId, String message) throws InvalidInputException {
        if (postId == null || postId.length() == 0) {
            throw new InvalidInputException(message+" is NULL");
        }
    }
}
