package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.*;
import com.tanmesh.splatter.service.IUserService;
import com.tanmesh.splatter.service.UserAuthService;
import com.tanmesh.splatter.wsRequestModel.UserData;
import com.tanmesh.splatter.wsRequestModel.UserLoginData;
import com.tanmesh.splatter.wsResponseModel.UserAuthResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user_auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    private IUserService userService;

    public AuthResource(IUserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUpUser(UserData userData) throws ApiException {
        User user = null;
        Response response;
        try {
            user = userService.signUpUser(userData);
            response = Response.status(Response.Status.ACCEPTED).entity(true).build();
        } catch (InvalidInputException e) {
            String error = "User signup data is not valid, try again with valid information";
            response = Response.status(Response.Status.EXPECTATION_FAILED).entity(error).build();
        } catch (EmailIdAlreadyRegistered e) {
            String error = "Email Id already registered, please login instead";
            response = Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        UserAuthResponse userAuthResponse;
        if (user != null) {
            userAuthResponse = new UserAuthResponse(user, response.getStringHeaders());
        } else {
            userAuthResponse = new UserAuthResponse(response.getStringHeaders());
        }
        return userAuthResponse;
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public UserAuthResponse logInUser(UserLoginData userLoginData) throws ApiException {
        String emailId = userLoginData.getEmailId();
        String password = userLoginData.getPassword();
        User user = null;
        Response response;
        try {
            user = userService.logInUser(emailId, password);
            response = Response.status(Response.Status.ACCEPTED).entity(true).build();
        } catch (InvalidInputException e) {
            String error = "User login data is null, try again with valid information";
            response = Response.status(Response.Status.EXPECTATION_FAILED).entity(error).build();
        } catch (EmailIdNotRegistered e) {
            String error = "Email Id not registered, please signup instead";
            response = Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        } catch (IncorrectPassword e) {
            String error = "incorrect password, please provide valid password";
            response = Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
        }

        UserAuthResponse userAuthResponse;
        if (user != null) {
            userAuthResponse = new UserAuthResponse(user, response.getStringHeaders());
            userAuthResponse.setAccessToken(UserAuthService.getAccessToken(user));
        } else {
            userAuthResponse = new UserAuthResponse(response.getStringHeaders());
        }
        return userAuthResponse;
    }

}
