package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.TagDAO;
import com.tanmesh.splatter.dao.UserPostDAO;
import com.tanmesh.splatter.entity.Tag;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.exception.PostNotFoundException;
import com.tanmesh.splatter.wsRequestModel.UserPostData;
import org.apache.commons.io.FileUtils;
import org.mongodb.morphia.Key;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserPostService implements IUserPostService {
    private UserPostDAO userPostDAO;
    private TagDAO tagDAO;

    public UserPostService(UserPostDAO userPostDAO, TagDAO tagDAO) {
        this.userPostDAO = userPostDAO;
        this.tagDAO = tagDAO;
    }

    @Override
    public void addPost(String userId,UserPostData userPostData) throws InvalidInputException , IOException{
        sanityCheck(userId, "userId");

        String location = userPostData.getLocation();
        sanityCheck(location, "location");

        String cuisineName = userPostData.getCuisineName();
        sanityCheck(cuisineName,"cuisine name");

        String image = userPostData.getImage();
        sanityCheck(image, "image");

        List<String> tags = userPostData.getTags();
        if(tags.isEmpty()) {
            throw new InvalidInputException("missing tags in user post");
        }

        UserPost userPost = new UserPost();
        userPost.setUserEmailId(userId);
        userPost.setLocation(location);
        userPost.setCuisineName(cuisineName);
        userPost.setTags(tags);

        byte[] fileContent = FileUtils.readFileToByteArray(new File(image));
        String encodedImg = Base64.getEncoder().encodeToString(fileContent);
        userPost.setImage(encodedImg);
        userPostDAO.save(userPost);

        for(String tagName : tags) {
            if (tagName == null || tagName.length() == 0) {
                throw new InvalidInputException("tagName is empty");
            }
            addTagHelper(tagName);
        }
    }

    private void addTagHelper(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        tagDAO.save(tag);
    }

    @Override
    public void deletePost(String postId) throws InvalidInputException {
        sanityCheck(postId, "postId");
        UserPost userPost = userPostDAO.getPost("postId", postId);
        userPostDAO.delete(userPost);
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
    public List<UserPost> getAllPostOfUser(String userEmailId) throws InvalidInputException {
        sanityCheck(userEmailId, "userEmailId");
        return userPostDAO.getAllPostOfUser("userEmailId", userEmailId);
    }

    private void sanityCheck(String postId, String message) throws InvalidInputException {
        if (postId == null || postId.length() == 0) {
            throw new InvalidInputException(message+" is NULL");
        }
    }
}
