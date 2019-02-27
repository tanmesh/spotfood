package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.ApiException;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.IUserPostService;
import com.tanmesh.splatter.wsRequestModel.UserPostData;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/user_post")
public class UserPostResource {
    private IUserPostService userPostService;

    public UserPostResource(IUserPostService userPostService) {
        this.userPostService = userPostService;
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean addPostDetails(UserPostData userPostData) throws ApiException {
        try {
            String postId = userPostData.getPostId();
            List<String> tagList = userPostData.getTags();
            String location = userPostData.getLocation();
            String authorName = userPostData.getAuthorName();
            userPostService.addPost(postId, tagList, location, authorName);
        } catch (InvalidInputException e) {
            throw new ApiException(Response.Status.EXPECTATION_FAILED, "unable to addPost");
        }
        return true;
    }

    // TODO: "edit" API
//    @POST
//    @Path("edit")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public boolean editPostDetails(UserPostData userPostData) throws ApiException {
//        try {
//            String postId = userPostData.getPostId();
//            List<String> tagList = userPostData.getTags();
//            String location = userPostData.getLocation();
//            String authorName = userPostData.getAuthorName();
//            userPostService.editPost(postId, tagList, location, authorName);
//        } catch (InvalidInputException e) {
//            throw new ApiException(Response.Status.EXPECTATION_FAILED, "unable to editPost");
//        }
//        return true;
//    }

    @GET
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    public UserPost getPostDetails(String postId) throws ApiException {
        UserPost userPost;
        try {
            userPost = userPostService.getPost(postId);
        } catch (InvalidInputException e) {
            throw new ApiException(Response.Status.EXPECTATION_FAILED, "unable to getPost");
        }
        return userPost;
    }

    @GET
    @Path("get_all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserPost> getAllPostDetails(String postId) throws ApiException {
        List<UserPost> userPost;
        try {
            userPost = userPostService.getAllPostOfUser(postId);
        } catch (InvalidInputException e) {
            throw new ApiException(Response.Status.EXPECTATION_FAILED, "unable to getAllPost");
        }
        return userPost;
    }

    @GET
    @Path("like")
    @Produces(MediaType.APPLICATION_JSON)
    public UserPost likePostDetails(String postId) throws ApiException {
        UserPost userPost;
        try {
            userPost = userPostService.likePost(postId);
        } catch (InvalidInputException e) {
            throw new ApiException(Response.Status.EXPECTATION_FAILED, "unable to likePost");
        }
        return userPost;
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean deletePostDetails(String postId) throws ApiException {
        try {
            userPostService.deletePost(postId);
        } catch (InvalidInputException e) {
            throw new ApiException(Response.Status.EXPECTATION_FAILED, "unable to deletePost");
        }
        return true;
    }

//    @POST
//    @Path("save")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public boolean savePostDetails(String imageFileInHex) throws ApiException {
//        try {
//            userPostService.savePost(imageFileInHex);
//        } catch (InvalidInputException e) {
//            throw new ApiException(Response.Status.EXPECTATION_FAILED, "unable to savePost");
//        }
//        return true;
//    }

}
