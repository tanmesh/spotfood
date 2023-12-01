package com.tanmesh.spotfood.resources;


import com.google.common.base.Preconditions;
import com.tanmesh.spotfood.authentication.UserSession;
import com.tanmesh.spotfood.service.ITagService;
import com.tanmesh.spotfood.wsRequestModel.TagData;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/tag")
public class TagResource {
    private ITagService tagService;

    public TagResource(ITagService tagService) {
        this.tagService = tagService;
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTag(@Auth UserSession userSession, TagData tagData) {
        Preconditions.checkNotNull(tagData.getName(), "tag name should not be null");
        tagService.addTag(tagData.getName());
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @GET
    @Path("get_all")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllTag(@Auth UserSession userSession) {
        List<TagData> tags;
        try {
            tags = tagService.getAllTag();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(tags).build();
    }

    @GET
    @Path("autocomplete/{prefix}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllTag(@Auth UserSession userSession, @PathParam("prefix") String prefix) {
        List<String> suggestedTags;
        try {
            suggestedTags = tagService.autocompleteTags(prefix);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(suggestedTags).build();
    }
}
