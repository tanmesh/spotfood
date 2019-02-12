package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.service.UserService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/debug")
public class DebugResource {
    private UserService userService;

    public DebugResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("exist")
    public boolean existenceUser(@QueryParam("userId") int userID) {
        return userService.userExists(userID);
    }

    @GET
    @Path("get_all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUserInfo(@QueryParam("userId") int userID) {
         return userService.userInfo();

    }
}
