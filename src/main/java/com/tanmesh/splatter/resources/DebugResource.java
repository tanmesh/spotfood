package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.IUserService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/debug")
public class DebugResource {
    private IUserService userService;

    public DebugResource(IUserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("exist")
    public boolean existenceUser(@QueryParam("emailId") String emailId) throws InvalidInputException {
        return userService.userExists(emailId);
    }

    @GET
    @Path("get_all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUserInfo(@QueryParam("emailId") String emailId) throws InvalidInputException {
        return userService.getAllUserInfo();
    }
}
