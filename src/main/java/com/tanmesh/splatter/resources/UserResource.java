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
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean addUser(UserData userData) {
        try {
            return userService.addUser(userData);
        } catch (InvalidInputException e) {
            return false;
        }
    }

    @POST
    @Path("remove")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean removeUser(UserData userData) {
        try {
            return userService.removeUser(userData.getFirstName(), userData.getLastName());
        } catch (InvalidInputException e) {
            return false;
        }
    }
}
