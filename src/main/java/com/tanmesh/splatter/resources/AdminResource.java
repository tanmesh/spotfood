package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.entity.Tag;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.IUserService;
import com.tanmesh.splatter.service.TagService;
import com.tanmesh.splatter.wsRequestModel.TagData;
import com.tanmesh.splatter.wsRequestModel.UserData;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("admin")
public class AdminResource {
    private IUserService userService;
    private TagService tagService;

    public AdminResource(IUserService userService, TagService tagService) {
        this.userService = userService;
        this.tagService = tagService;
    }

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeUser(UserData userData){
        try {
            userService.deleteUser(userData.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }


    // Admin APIs to add/delete/get tags, same APIs are present in tag resources as well
    @POST
    @Path("add_tag")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTag(TagData tagData) {
        try {
            tagService.addTag(tagData);
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

    @GET
    @Path("/get_all_tag")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTag() {
        List<Tag> tags;
        try {
            tags = tagService.getAllTag();
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(tags).build();
    }
}
