package com.tanmesh.splatter.resources;


import com.google.common.base.Preconditions;
import com.tanmesh.splatter.authentication.UserSession;
import com.tanmesh.splatter.entity.Tag;
import com.tanmesh.splatter.service.ITagService;
import com.tanmesh.splatter.wsRequestModel.TagData;
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

    @Path("get_all")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllTag(@Auth UserSession userSession) {
        List<Tag> tags;
        try {
            tags = tagService.getAllTag();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(tags).build();
    }
}
