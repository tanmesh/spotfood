package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.IUserService;
import com.tanmesh.splatter.wsRequestModel.UserData;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserResource {
    private IUserService userService;

    public UserResource(IUserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUpUser(UserData userData) {
        try {
            userService.signUpUser(userData);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logInUser(UserData userData) {
        String emailId = userData.getEmailId();
        String password = userData.getPassword();
        try {
            userService.logInUser(emailId, password);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @POST
    @Path("follow")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response followTag(UserData userData) {
        try {
            userService.followTag(userData.getTag(), userData.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @POST
    @Path("unfollow")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response unFollowTag(UserData userData) {
        try {
            userService.unFollowTag(userData.getTag(), userData.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @POST
    @Path("profile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfile(String emailId) {
        User user;
        try {
            user = userService.userProfile(emailId);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(user).build();
    }

    @GET
    @Path("feed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserFeed(String emailId) {
        try {
            userService.getUserFeed(emailId);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }
}
