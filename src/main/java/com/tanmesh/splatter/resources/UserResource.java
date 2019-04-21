package com.tanmesh.splatter.resources;

import com.google.common.collect.Lists;
import com.tanmesh.splatter.authentication.UserSession;
import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.ApiException;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.IUserService;
import com.tanmesh.splatter.wsRequestModel.UserData;
import io.dropwizard.auth.Auth;
import sun.plugin.util.UserProfile;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/user")
public class UserResource {
    private IUserService userService;

    public UserResource(IUserService userService) {
        this.userService = userService;
    }

//    @Path("/followTag")
//    @POST
//    public Response followTag(@Auth UserSession userSession, String tag) {
//        try {
//            userService.followTag(userSession.getEmailId(), tag);
//        } catch (InvalidInputException e) {
//            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
//        }
//        return Response.status(Response.Status.ACCEPTED).entity(true).build();
//    }
//
//
//    @Path("/unfollowTag")
//    @POST
//    public Response unfollowTag(@Auth UserSession userSession, String tag) {
//        try {
//            userService.unFollowTag(userSession.getEmailId(), tag);
//        } catch (InvalidInputException e) {
//            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
//        }
//        return Response.status(Response.Status.ACCEPTED).entity(true).build();
//    }

    @Path("/getFollowingTags")
    @GET
    public Set<String> getFollowingTags(@Auth UserSession userSession) {
        return userService.getFollowingTags(userSession.getEmailId());
    }

    @GET
    @Path("profile")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfile(@Auth UserSession userSession) {
        User user;
        try {
            user = userService.userProfile(userSession.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(user).build();
    }

    // TODO: complete feed API
//    @GET
//    @Path("feed")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getUserFeed(String emailId) {
//        try {
//            userService.getUserFeed(emailId);
//        } catch (InvalidInputException e) {
//            return Response.status(Response.Status.EXPECTATION_FAILED).entity(e.getMessage()).build();
//        }
//        return Response.status(Response.Status.ACCEPTED).entity(true).build();
//    }
}
