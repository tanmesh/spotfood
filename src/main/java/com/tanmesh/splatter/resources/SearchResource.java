package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.authentication.UserSession;
import com.tanmesh.splatter.service.ISearchService;
import com.tanmesh.splatter.wsRequestModel.SearchData;
import com.tanmesh.splatter.wsRequestModel.UserPostData;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

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
    public Response getNearbySearchResult(@Auth UserSession userSession, SearchData searchData) {
        String emailId = userSession.getEmailId();

        Set<UserPostData> userPostsData = new HashSet<>();
        switch (searchData.getType()) {
            case TAG:
                userPostsData = searchService.getSearchTagsResults(emailId, searchData);
                break;
            case LOCALITY:
                userPostsData = searchService.getSearchLocalityResults(emailId, searchData);
                break;
        }

        return Response.status(Response.Status.ACCEPTED).entity(userPostsData).build();
    }
}
