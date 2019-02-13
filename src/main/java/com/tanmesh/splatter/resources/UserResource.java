package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.ApiException;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.IUserService;
import com.tanmesh.splatter.wsRequestModel.UserData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public boolean signUpUser(UserData userData) throws ApiException {
        try {
            userService.signUpUser(userData);
        } catch (InvalidInputException e) {
            throw new ApiException(Response.Status.EXPECTATION_FAILED, "unable to signUp");
        }
        return true;
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean logInUser(UserData userData) throws ApiException {
        try {
            userService.logInUser(userData);
        } catch (InvalidInputException e) {
            throw new ApiException(Response.Status.EXPECTATION_FAILED, "unable to logIn");
        }
        return true;
    }

    @POST
    @Path("follow")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean followTag(UserData userData) throws ApiException {
        try {
            userService.followTag(userData.getTag(), userData.getEmailId());
        } catch (InvalidInputException e) {
            throw new ApiException(Response.Status.NOT_ACCEPTABLE, "following UserId does not exist");
        }
        return true;
    }

    @POST
    @Path("unfollow")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean unFollowTag(UserData userData) throws ApiException {
        try {
            userService.followTag(userData.getTag(), userData.getEmailId());
        } catch (InvalidInputException e) {
            throw new ApiException(Response.Status.NOT_ACCEPTABLE, "unfollowing UserId does not exist");
        }
        return true;
    }

    @POST
    @Path("profile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserProfile(String emailId) throws ApiException {
        User user;
        try {
            user = userService.userProfile(emailId);
        } catch (InvalidInputException e) {
            throw new ApiException(Response.Status.NOT_ACCEPTABLE, "no userProfile found");
        }
        return user;
    }
}
