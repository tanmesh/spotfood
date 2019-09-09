package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.authentication.UserSession;
import com.tanmesh.splatter.entity.search.SearchEntity;
import com.tanmesh.splatter.service.ISearchService;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by tanmesh
 * Date: 2019-09-09
 * Time: 11:32
 */

@Path("/search")
public class SearchResource {
    private ISearchService searchService;

    public SearchResource(ISearchService searchService) {
        this.searchService = searchService;
    }

    @POST
    @Path("nearby")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNearbySearchResult(@Auth UserSession userSession, SearchEntity searchEntity, @QueryParam("near") Boolean near) {
//        String emailId = userSession.getEmailId();
//        Set<UserPost> userPosts = searchService.getNearbySearchResult(emailId, searchEntity, near);
//        return Response.status(Response.Status.ACCEPTED).entity(userPosts).build();
        String emailId = userSession.getEmailId();
        return Response.status(Response.Status.ACCEPTED).entity(searchService.getNearbySearchResult(emailId, searchEntity, near)).build();
    }
}
