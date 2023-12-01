package com.tanmesh.spotfood.resources;

import com.tanmesh.spotfood.authentication.UserSession;
import com.tanmesh.spotfood.service.ISearchService;
import com.tanmesh.spotfood.wsRequestModel.SearchData;
import com.tanmesh.spotfood.wsRequestModel.UserPostData;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

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
    @Path("nearby/{offset}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNearbySearchResult(@Auth UserSession userSession, SearchData searchData, @PathParam("offset") int offset) {
        String emailId = userSession.getEmailId();

        List<UserPostData> userPostsData = new ArrayList<>();
        switch (searchData.getType()) {
            case TAG:
                userPostsData = searchService.getSearchTagsResults(emailId, searchData, offset);
                break;
            case LOCALITY:
                userPostsData = searchService.getSearchLocalityResults(emailId, searchData, offset);
                break;
        }

        return Response.status(Response.Status.ACCEPTED).entity(userPostsData).build();
    }
}
