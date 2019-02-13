package com.tanmesh.splatter.resources;


import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.TagService;
import com.tanmesh.splatter.wsRequestModel.TagData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/tag")
public class TagResource {
    private TagService tagService;

    public TagResource(TagService tagService) {
        this.tagService = tagService;
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean addTag(TagData tagData) {
        try {
            return tagService.addTag(tagData);
        } catch (InvalidInputException e) {
            return false;
        }
    }
}
