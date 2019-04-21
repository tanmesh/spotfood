package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.IUserService;
import com.tanmesh.splatter.service.TagService;
import com.tanmesh.splatter.wsRequestModel.TagData;
import com.tanmesh.splatter.wsRequestModel.UserData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/admin")
public class AdminResource {
    private IUserService userService;
    private TagService tagService;

    public AdminResource(IUserService userService, TagService tagService) {
        this.userService = userService;
        this.tagService = tagService;
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeUser(UserData userData){
        try {
            userService.deleteUser(userData.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @POST
    @Path("delete_tag")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeTag(TagData tagData){
        try {
            tagService.deleteTag(tagData.getName());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    // TODO "get_all_post" API
}
