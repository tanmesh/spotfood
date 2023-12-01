package com.tanmesh.spotfood.resources;

import com.google.common.base.Preconditions;
import com.tanmesh.spotfood.exception.InvalidInputException;
import com.tanmesh.spotfood.service.ITagService;
import com.tanmesh.spotfood.service.IUserService;
import com.tanmesh.spotfood.wsRequestModel.TagData;
import com.tanmesh.spotfood.wsRequestModel.UserData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/admin")
public class AdminResource {
    private IUserService userService;
    private ITagService tagService;

    public AdminResource(IUserService userService, ITagService tagService) {
        this.userService = userService;
        this.tagService = tagService;
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeUser(UserData userData){
        try {
            Preconditions.checkNotNull(userData.getEmailId(), "email Id should not be null");

            userService.deleteUser(userData.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @POST
    @Path("delete_tag")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeTag(TagData tagData){
        try {
            Preconditions.checkNotNull(tagData.getName(), "Name should not be null");
            tagService.deleteTag(tagData.getName());
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }
}
