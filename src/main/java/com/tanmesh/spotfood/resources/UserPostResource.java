package com.tanmesh.spotfood.resources;

import com.google.common.base.Preconditions;
import com.tanmesh.spotfood.authentication.UserSession;
import com.tanmesh.spotfood.entity.UserPost;
import com.tanmesh.spotfood.exception.InvalidInputException;
import com.tanmesh.spotfood.exception.PostNotFoundException;
import com.tanmesh.spotfood.service.AccessTokenService;
import com.tanmesh.spotfood.service.IFeedService;
import com.tanmesh.spotfood.service.IUserPostService;
import com.tanmesh.spotfood.wsRequestModel.UserPostData;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Path("/user_post")
public class UserPostResource {
    private IUserPostService userPostService;
    private IFeedService feedService;
    private AccessTokenService accessTokenService;

    public UserPostResource(IUserPostService userPostService, AccessTokenService accessTokenService, IFeedService feedService) {
        this.userPostService = userPostService;
        this.accessTokenService = accessTokenService;
        this.feedService = feedService;
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPostDetails(@Auth UserSession userSession, UserPostData userPostData) {
        try {
            Preconditions.checkNotNull(userPostData.getTagList(), "tags should not be null");
            Preconditions.checkNotNull(userPostData.getRestaurantName(), "location should not be null");
            Preconditions.checkNotNull(userPostData.getImgUrl(), "image should not be null");

            userPostService.addPost(userPostData, userSession.getEmailId());
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @POST
    @Path("add_dummy")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDummyPostDetails() {
        try {
            userPostService.addDummyPost();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @GET
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostDetails(@Auth UserSession userSession, @QueryParam("postId") String postId) {
        UserPost userPost;
        try {
            Preconditions.checkNotNull(postId, "postId id should not be null");
            userPost = userPostService.getPost(postId);
        } catch (InvalidInputException | PostNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(userPost).build();
    }

    @POST
    @Path("like")
    @Produces(MediaType.APPLICATION_JSON)
    public Response likePostDetails(@Auth UserSession userSession, @QueryParam("postId") String postId) {
        UserPost userPost;
        try {
            Preconditions.checkNotNull(postId, "post id should not be null");
            userPost = userPostService.likePost(userSession.getEmailId(), postId);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (PostNotFoundException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(userPost).build();
    }

    @POST
    @Path("unlike")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unlikePostDetails(@Auth UserSession userSession, @QueryParam("postId") String postId) {
        UserPost userPost;
        try {
            Preconditions.checkNotNull(postId, "post id should not be null");
            userPostService.unlikePost(userSession.getEmailId(), postId);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (PostNotFoundException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePostDetails(@Auth UserSession userSession, @QueryParam("postId") String postId) {
        try {
            Preconditions.checkNotNull(postId, "post id should not be null");
            userPostService.deletePost(postId);
        } catch (InvalidInputException | PostNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @GET
    @Path("feeds")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userFeeds(@Auth UserSession userSession) throws InvalidInputException {
        String emailId = userSession.getEmailId();
        List<UserPostData> feeds = feedService.getUserFeed(emailId, -1);
        return Response.status(Response.Status.ACCEPTED).entity(feeds).build();
    }

    @GET
    @Path("get_user_posts/{startAfter}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPostDetails(@Auth UserSession userSession, @QueryParam("authorEmailId") String authorEmailId, @PathParam("startAfter") int startAfter) {
        List<UserPostData> userPostData;
        try {
            Preconditions.checkNotNull(authorEmailId, "author email id should not be null");
            userPostData = userPostService.getAllPostOfUser(authorEmailId, startAfter);
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(userPostData).build();
    }

    @GET
    @Path("feeds/{startAfter}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userFeeds(@Auth UserSession userSession, @PathParam("startAfter") int startAfter) throws InvalidInputException {
        List<UserPostData> feeds = new ArrayList<>();
        try {
            String emailId = userSession.getEmailId();
            feeds = feedService.getUserFeed(emailId, startAfter);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(feeds).build();
    }

    @GET
    @Path("explore/{startAfter}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userExplore(@PathParam("startAfter") int startAfter, @QueryParam("emailId") String emailId) {
        List<UserPostData> feeds;
        System.out.println(emailId);
        try {
            if (Objects.equals(emailId, "null")) {
                feeds = userPostService.getAllPost(startAfter);
            } else {
                feeds = feedService.getUserExplore(emailId, startAfter);
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(feeds).build();
    }

    @POST
    @Path("/generateFeed")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateFeed() {
        try {
            feedService.generateFeed();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @POST
    @Path("/generateExplore")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateExplore() {
        try {
            feedService.generateExplore();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
        return Response.status(Response.Status.ACCEPTED).build();
    }
}
