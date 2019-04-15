package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.EmailIdAlreadyRegistered;
import com.tanmesh.splatter.exception.EmailIdNotRegistered;
import com.tanmesh.splatter.exception.IncorrectPassword;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.service.AuthService;
import com.tanmesh.splatter.service.IUserService;
import com.tanmesh.splatter.wsRequestModel.UserData;
import com.tanmesh.splatter.wsRequestModel.UserLoginData;
import com.tanmesh.splatter.wsResponseModel.AuthResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    private IUserService userService;

    public AuthResource(IUserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUpUser(UserData userData) {
        try {
            userService.signUpUser(userData);
            return Response.status(Response.Status.ACCEPTED).entity(true).build();
        } catch (InvalidInputException e) {
            String error = "User signup data is not valid, try again with valid information";
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(error).build();
        } catch (EmailIdAlreadyRegistered e) {
            String error = "Email Id already registered, please login instead";
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logInUser(UserLoginData userLoginData) {
        String emailId = userLoginData.getEmailId();
        String password = userLoginData.getPassword();

        try {
            User user = userService.logInUser(emailId, password);
            AuthResponse authResponse = new AuthResponse(user, AuthService.getAccessToken(user));
            return Response.status(Response.Status.ACCEPTED).entity(authResponse).build();
        } catch (InvalidInputException e) {
            String error = "User login data is null, try again with valid information";
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(error).build();
        } catch (EmailIdNotRegistered e) {
            String error = "Email Id not registered, please signup instead";
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        } catch (IncorrectPassword e) {
            String error = "incorrect password, please provide valid password";
            return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
        }


    }

}
