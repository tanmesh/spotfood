package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.IUserService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/debug")
public class DebugResource {
    private IUserService userService;

    public DebugResource(IUserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("exist")
    public Response existenceUser(@QueryParam("emailId") String emailId) {
        try {
            userService.userExists(emailId);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @GET
    @Path("get_all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfo(@QueryParam("emailId") String emailId) {
        List<User> users;
        try {
            users = userService.userInfo();
        }catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(users).build();
    }
}
