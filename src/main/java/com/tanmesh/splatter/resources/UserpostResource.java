package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.exception.ApiException;
import com.tanmesh.splatter.wsRequestModel.UserpostData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/user_post")
public class UserpostResource {

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean addPost(UserpostData userpostData) throws ApiException {

        return true;
    }
}
