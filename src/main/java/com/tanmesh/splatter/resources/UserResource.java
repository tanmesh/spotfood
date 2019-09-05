package com.tanmesh.splatter.resources;

import com.google.common.base.Preconditions;
import com.tanmesh.splatter.authentication.UserSession;
import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.AccessTokenService;
import com.tanmesh.splatter.service.IUserService;
import com.tanmesh.splatter.wsRequestModel.UserData;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/user")
public class UserResource {
    private IUserService userService;
    private AccessTokenService accessTokenService;

    public UserResource(IUserService userService, AccessTokenService accessTokenService) {
        this.userService = userService;
        this.accessTokenService = accessTokenService;
    }

    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUpUser(UserData userData) {
        try {
            userService.signUpUser(userData);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logInUser(UserData userData) {
        String emailId = userData.getEmailId();
        String password = userData.getPassword();
        UserSession userSession;
        try {
            userSession = logInUserHelper(emailId, password);
        } catch (InvalidInputException | NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(userSession).build();
    }

    private UserSession logInUserHelper(String emailId, String inputPassword) throws Exception {
        Preconditions.checkNotNull(emailId, "email id should not be null");
        Preconditions.checkNotNull(inputPassword, "input password should not be null");

        User user = userService.getUserProfile(emailId);
        UserSession userSession;
        try {
            if (!(user.getPassword()).equals(inputPassword)) {
                throw new InvalidInputException("Password is wrong");
            }
            String accessToken = UUID.randomUUID().toString();
            userSession = new UserSession(accessToken, user.getEmailId());
            if (!accessTokenService.saveAccessToken(userSession)) {
                throw new InvalidInputException("Unable to save the new token");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return userSession;
    }

    @POST
    @Path("logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logOutUser(@Auth UserSession userSession) {
        accessTokenService.removeAccessToken(userSession.getAccessToken());
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }


    @POST
    @Path("follow")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response followTag(@Auth UserSession userSession, UserData userData) {
        try {
            userService.followTag(userData.getTag(), userData.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @POST
    @Path("unfollow")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unFollowTag(@Auth UserSession userSession, UserData userData) {
        try {
            userService.unFollowTag(userData.getTag(), userData.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @GET
    @Path("profile")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfile(@Auth UserSession userSession) {
        User user;
        try {
            user = userService.getUserProfile(userSession.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(user).build();
    }

    // TODO: "feed" API
}
