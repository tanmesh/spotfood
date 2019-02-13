package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.UserService;
import com.tanmesh.splatter.wsRequestModel.UserData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/user")
public class UserResource {
    private UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("remove")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean removeUser(UserData userData) {
        try {
            return userService.removeUser(userData.getFirstName());
        } catch (InvalidInputException e) {
            return false;
        }
    }

    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean signUpUser(UserData userData) {
        try {
            return userService.signUpUser(userData);
        } catch (InvalidInputException e) {
            return false;
        }
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public String logInUser(UserData userData) {
        try {
            return userService.logInUser(userData);
        } catch (InvalidInputException e) {
            return "Error";
        }
    }

    @POST
    @Path("follow")
    @Consumes(MediaType.APPLICATION_JSON)
    public String followUser(UserData userData) {
        try {
            return userService.followUser(userData);
        } catch (InvalidInputException e) {
            return "Error";
        }
    }
}
