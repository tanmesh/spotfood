package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.authentication.UserSession;
import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.IUserService;
import com.tanmesh.splatter.wsRequestModel.UserActivityData;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private IUserService userService;

    public UserResource(IUserService userService) {
        this.userService = userService;
    }


    @Path("/followTag")
    @POST
    public Response followTag(@Auth UserSession userSession, String tag) {
        try {
            userService.followTag(userSession.getEmailId(), tag);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }


    @Path("/unfollowTag")
    @POST
    public Response unfollowTag(@Auth UserSession userSession, String tag) {
        try {
            userService.unFollowTag(userSession.getEmailId(), tag);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @Path("/getFollowingTags")
    @GET
    public Set<String> getFollowingTags(@Auth UserSession userSession) {
        return userService.getFollowingTags(userSession.getEmailId());
    }

    @GET
    @Path("/profile")
    public Response getProfile(@Auth UserSession userSession) {
        User user;
        try {
            user = userService.getUserProfile(userSession.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(user).build();
    }

    @GET
    @Path("/feed")
    public Response getFeed(@Auth UserSession userSession) {
        Set<UserPost> feeds;
        try {
            feeds = userService.getUserFeed(userSession.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(feeds).build();
    }
}
