package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.exception.PostNotFoundException;
import com.tanmesh.splatter.service.IUserPostService;
import com.tanmesh.splatter.wsRequestModel.UserPostData;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
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
    public Response addPostDetails(UserPostData userPostData) {
        try {
            String postId = userPostData.getPostId();
            List<String> tagList = userPostData.getTags();
            String location = userPostData.getLocation();
            String authorName = userPostData.getAuthorEmailId();
            String encodedImgFilePath = userPostData.getEncodedImgFilePath();
            userPostService.addPost(postId, tagList, location, authorName, encodedImgFilePath);
        } catch (InvalidInputException | IOException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @GET
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostDetails(@QueryParam("postId") String postId) {
        UserPost userPost;
        try {
            userPost = userPostService.getPost(postId);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(userPost).build();
    }

    @GET
    @Path("get_all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPostDetails(@QueryParam("authorEmailId") String authorEmailId) {
        List<UserPost> userPost;
        try {
            userPost = userPostService.getAllPostOfUser(authorEmailId);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(userPost).build();
    }

    @GET
    @Path("like")
    @Produces(MediaType.APPLICATION_JSON)
    public Response likePostDetails(@QueryParam("postId") String postId) {
        UserPost userPost;
        try {
            userPost = userPostService.likePost(postId);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (PostNotFoundException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(userPost).build();
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePostDetails(String postId) {
        try {
            userPostService.deletePost(postId);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    // TODO: "edit" API

    // TODO: "save" API

}
