package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.IUserService;
import com.tanmesh.splatter.wsRequestModel.UserData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/admin")
public class AdminResource {
    private IUserService userService;

    public AdminResource(IUserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean removeUser(UserData userData) throws InvalidInputException {
        return userService.deleteUser(userData.getEmailId());
    }
}
