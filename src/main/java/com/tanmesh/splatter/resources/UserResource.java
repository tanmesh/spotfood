package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.authentication.UserSession;
import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.IUserService;
import com.tanmesh.splatter.wsRequestModel.UserActivityData;
import com.tanmesh.splatter.wsResponseModel.UserProfileResponse;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;
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

    @Path("/getFollowingTagCount")
    @GET
    public int getFollowingTagCount(@Auth UserSession userSession) {
        return userService.getFollowingTagCount(userSession.getEmailId());
    }

    @Path("/followUser")
    @POST
    public Response followUser(@Auth UserSession userSession, String userId) {
        try {
            userService.followUser(userSession.getEmailId(), userId);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @Path("/unfollowUser")
    @POST
    public Response unfollowUser(@Auth UserSession userSession, String userId) {
        try {
            userService.unfollowUser(userSession.getEmailId(), userId);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @Path("/getFollowingUsers")
    @GET
    public Set<String> getFollowingUsers(@Auth UserSession userSession) {
        return userService.getFollowingUsers(userSession.getEmailId());
    }

    @GET
    @Path("/profile")
    public Response getProfile(@Auth UserSession userSession) {
        UserProfileResponse userProfileResponse;
        try {
            userProfileResponse = userService.getUserProfile(userSession.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(userProfileResponse).build();
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
