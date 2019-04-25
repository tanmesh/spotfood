package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.authentication.UserSession;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.exception.PostNotFoundException;
import com.tanmesh.splatter.service.IUserPostService;
import com.tanmesh.splatter.wsRequestModel.UserPostData;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/user_post")
@Produces(MediaType.APPLICATION_JSON)
public class UserPostResource {
    private IUserPostService userPostService;

    public UserPostResource(IUserPostService userPostService) {
        this.userPostService = userPostService;
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(@Auth UserSession userSession, UserPostData userPostData) {
        try {
            userPostService.addPost(userSession.getEmailId(), userPostData);
        } catch (InvalidInputException | IOException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @GET
    @Path("/get_all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Auth UserSession userSession) {
        List<UserPost> userPost;
        try {
            userPost = userPostService.getAllPostOfUser(userSession.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(userPost).build();
    }


    @POST
    @Path("/like")
    @Produces(MediaType.APPLICATION_JSON)
    public Response like(String postId) {
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
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(String postId) {
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
