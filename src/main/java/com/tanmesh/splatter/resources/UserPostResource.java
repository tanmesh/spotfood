package com.tanmesh.splatter.resources;

import com.google.common.base.Preconditions;
import com.tanmesh.splatter.authentication.UserSession;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.exception.PostNotFoundException;
import com.tanmesh.splatter.service.AccessTokenService;
import com.tanmesh.splatter.service.IUserPostService;
import com.tanmesh.splatter.wsRequestModel.UserPostData;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Path("/user_post")
public class UserPostResource {
    private IUserPostService userPostService;
    private AccessTokenService accessTokenService;

    public UserPostResource(IUserPostService userPostService, AccessTokenService accessTokenService) {
        this.userPostService = userPostService;
        this.accessTokenService = accessTokenService;
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPostDetails(@Auth UserSession userSession, UserPostData userPostData) {
        try {
            Preconditions.checkNotNull(userPostData.getTags(), "tags should not be null");
            Preconditions.checkNotNull(userPostData.getLocationName(), "location should not be null");
            Preconditions.checkNotNull(userPostData.getEncodedImgString(), "image should not be null");
            Preconditions.checkNotNull(userPostData.getLatitude(), "latitude should not be null");
            Preconditions.checkNotNull(userPostData.getLongitude(), "longitude should not be null");

            String emailId = userSession.getEmailId();

            //  TODO: need to work on the image
            userPostService.addPost(userPostData, emailId);
        } catch (InvalidInputException | IOException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @GET
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostDetails(@Auth UserSession userSession) {
        UserPost userPost;
        try {
            String emailId = userSession.getEmailId();
            userPost = userPostService.getPost(emailId);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(userPost).build();
    }

    @GET
    @Path("get_specific_user_posts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPostDetails(@Auth UserSession userSession, @QueryParam("authorEmailId") String authorEmailId) {
        List<UserPost> userPost;
        try {
            Preconditions.checkNotNull(authorEmailId, "author email id should not be null");
            userPost = userPostService.getAllPostOfUser(authorEmailId);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(userPost).build();
    }

    @GET
    @Path("like")
    @Produces(MediaType.APPLICATION_JSON)
    public Response likePostDetails(@Auth UserSession userSession, @QueryParam("postId") String postId) {
        UserPost userPost;
        try {
            Preconditions.checkNotNull(postId, "post id should not be null");
            userPost = userPostService.likePost(postId);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (PostNotFoundException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(userPost).build();
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePostDetails(@Auth UserSession userSession, String postId) {
        try {
            Preconditions.checkNotNull(postId, "post id should not be null");
            userPostService.deletePost(postId);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @GET
    @Path("feeds")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userFeeds(@Auth UserSession userSession) {
        String emailId = userSession.getEmailId();
        Set<UserPost> feeds = userPostService.getUserFeed(emailId);
        return Response.status(Response.Status.ACCEPTED).entity(feeds).build();
    }

    // TODO: "edit" API
}
