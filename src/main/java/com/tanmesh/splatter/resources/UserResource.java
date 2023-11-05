package com.tanmesh.splatter.resources;

import com.google.common.base.Preconditions;
import com.tanmesh.splatter.authentication.UserSession;
import com.tanmesh.splatter.exception.InvalidEmailId;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.AccessTokenService;
import com.tanmesh.splatter.service.IUserService;
import com.tanmesh.splatter.utils.CustomKafkaProducer;
import com.tanmesh.splatter.wsRequestModel.UserData;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Path("/user")
public class UserResource {
    private IUserService userService;
    private AccessTokenService accessTokenService;
    private CustomKafkaProducer _producer;
//    public UserResource(IUserService userService, AccessTokenService accessTokenService, KafkaProducerConfig kafkaProducerConfig) {
//        this.userService = userService;
//        this.accessTokenService = accessTokenService;
//        this._producer = new CustomKafkaProducer("topic-user", kafkaProducerConfig);
//    }

    public UserResource(IUserService userService, AccessTokenService accessTokenService) {
        this.userService = userService;
        this.accessTokenService = accessTokenService;
        this._producer = new CustomKafkaProducer("topic-user");
    }

    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUpUser(UserData userData) {
        try {
            if (userData == null) {
                throw new InvalidInputException("UserData is null");
            }
            String firstName = userData.getFirstName();
            String lastName = userData.getLastName();
            String nickName = userData.getNickName();
            String emailId = userData.getEmailId();
            String password = userData.getPassword();

            Preconditions.checkNotNull(firstName, "firstName should not be null");
            Preconditions.checkNotNull(lastName, "lastName should not be null");
            Preconditions.checkNotNull(nickName, "nickName should not be null");
            Preconditions.checkNotNull(emailId, "emailId should not be null");
            Preconditions.checkNotNull(password, "password should not be null");

            if (correctPattern(userData.getEmailId())) {
                userService.signUpUser(userData);
            }
        } catch (InvalidEmailId e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

        Response response = Response.status(Response.Status.ACCEPTED).entity(userData).build();
        _producer.send(response);
        return response;
    }

    private boolean correctPattern(String emailId) throws InvalidEmailId {
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(emailId);
        if (!matcher.matches()) {
            throw new InvalidEmailId("EmailId is wrong");
        }
        return true;
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logInUser(UserData userData) {
        String emailId = userData.getEmailId();
        String password = userData.getPassword();

        Preconditions.checkNotNull(userData.getEmailId(), "emailId should not be null");
        Preconditions.checkNotNull(userData.getPassword(), "password should not be null");
        UserSession userSession;
        try {
            userSession = userService.logInUser(emailId, password);
        } catch (NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(userSession).build();
    }

    @POST
    @Path("logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logOutUser(@Auth UserSession userSession) {
        accessTokenService.removeAccessToken(userSession.getAccessToken());
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @POST
    @Path("follow_tag")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response followTag(@Auth UserSession userSession, UserData userData) {
        try {
            Preconditions.checkNotNull(userData.getTagList(), "input password should not be null");
            userService.followTag(userData.getTagList(), userSession.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @POST
    @Path("unfollow_tag")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unFollowTag(@Auth UserSession userSession, UserData userData) {
        try {
            Preconditions.checkNotNull(userData.getTagList(), "input password should not be null");
            userService.unFollowTag(userData.getTagList(), userSession.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @POST
    @Path("follow_user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response followUser(@Auth UserSession userSession, UserData userData) {
        try {
            Preconditions.checkNotNull(userData.getEmailId(), "input password should not be null");
            userService.followUser(userData.getEmailId(), userSession.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @POST
    @Path("unfollow_user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unFollowUser(@Auth UserSession userSession, UserData userData) {
        try {
            Preconditions.checkNotNull(userData.getEmailId(), "input password should not be null");
            userService.unFollowUser(userData.getEmailId(), userSession.getEmailId());
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(true).build();
    }

    @GET
    @Path("profile")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfile(@Auth UserSession userSession) {
        Preconditions.checkNotNull(userSession.getEmailId(), "email Id should not be null");
        UserData userData = userService.getUserProfile(userSession.getEmailId());
        return Response.status(Response.Status.ACCEPTED).entity(userData).build();
    }

    @POST
    @Path("edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response edit(@Auth UserSession userSession, UserData userData) {
        UserData sendUserData;
        try {
            sendUserData = userService.edit(userSession.getEmailId(), userData);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("bad request").build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(sendUserData).build();
    }
}
