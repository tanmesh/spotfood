package com.tanmesh.splatter.resources;


import com.tanmesh.splatter.entity.Tag;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.TagService;
import com.tanmesh.splatter.wsRequestModel.TagData;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Path("tag")
public class TagResource {
    private TagService tagService;

    public TagResource(TagService tagService) {
        this.tagService = tagService;
    }


    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTag(TagData tagData) {
        try {
            tagService.addTag(tagData);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @GET
    @Path("/get_all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTag() {
        Set<Tag> tags;
        try {
            tags = tagService.getAllTag();
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(tags).build();
    }
}
