package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.IUserService;
import com.tanmesh.splatter.wsRequestModel.UserData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/admin")
public class AdminResource {
    private IUserService userService;

    public AdminResource(IUserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeUser(UserData userData){
        try {
            userService.deleteUser(userData.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    // TODO "get_all_post" API
}
