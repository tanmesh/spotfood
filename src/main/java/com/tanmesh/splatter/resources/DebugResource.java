package com.tanmesh.splatter.resources;

import com.google.common.base.Preconditions;
import com.tanmesh.splatter.authentication.UserSession;
import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.ITagService;
import com.tanmesh.splatter.service.IUserService;
import io.dropwizard.auth.Auth;

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
    private ITagService tagService;

    public DebugResource(IUserService userService, ITagService tagService) {
        this.userService = userService;
        this.tagService = tagService;
    }

    @GET
    @Path("exist")
    @Produces(MediaType.APPLICATION_JSON)
    public Response existenceUser(@Auth UserSession userSession, @QueryParam("emailId") String emailId) {
        try {
            userService.userExists(emailId);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(userSession).build();
    }

    @GET
    @Path("get_all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfo(@QueryParam("emailId") String emailId) {
        List<User> users;
        try {
            users = userService.userInfo();
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(users).build();
    }

    @GET
    @Path("autocompleteTag")
    @Produces(MediaType.APPLICATION_JSON)
    public Response autocompleteTag(@QueryParam("inputPrefix") String inputPrefix) {
        List<String> tags;
        try {
            Preconditions.checkNotNull(inputPrefix, "input prefix should not be null");
            tags = tagService.autocompleteTags(inputPrefix);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(tags).build();
    }
}
