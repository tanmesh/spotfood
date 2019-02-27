package com.tanmesh.splatter.resources;


import com.tanmesh.splatter.entity.Tag;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.TagService;
import com.tanmesh.splatter.wsRequestModel.TagData;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/tag")
public class TagResource {
    private TagService tagService;

    public TagResource(TagService tagService) {
        this.tagService = tagService;
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTag(TagData tagData) {
        try {
            boolean response = tagService.addTag(tagData);
            return Response.status(Response.Status.ACCEPTED).entity(response).build();
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("invalid input").build();
        }
    }

    @GET
    @Path("get_all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Tag> getAllTag(TagData tagData) throws InvalidInputException {
        return tagService.getAllTag(tagData);
    }
}
