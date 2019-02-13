package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.exception.ApiException;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.IUserPostService;
import com.tanmesh.splatter.wsRequestModel.UserPostData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/user_post")
public class UserPostResource {
    private IUserPostService userPostService;

    public UserPostResource(IUserPostService userPostService) {
        this.userPostService = userPostService;
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean addPostDetails(UserPostData userPostData) throws ApiException {
        String postId = userPostData.getPostId();
        List<String > tagList = userPostData.getTags();
        String location = userPostData.getLocation();
        String authorName = userPostData.getAuthorName();
        try {
            userPostService.addPost(postId, tagList, location, authorName);
        } catch (InvalidInputException e) {
            throw new ApiException(Response.Status.EXPECTATION_FAILED, "unable to addPost");
        }
        return true;
    }

    
}
