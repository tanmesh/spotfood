package com.tanmesh.splatter.resources;

import com.tanmesh.splatter.exception.*;
import com.tanmesh.splatter.service.IUserService;
import com.tanmesh.splatter.wsRequestModel.UserData;
import com.tanmesh.splatter.wsRequestModel.UserLoginData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthResource {
    private IUserService userService;

    public AuthResource(IUserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public String signUpUser(UserData userData) {
        String retString;
        try {
            if (userService.signUpUser(userData)) {
                retString = "SUCCESS";
            } else {
                retString = "FAILED";
            }
        } catch (InvalidInputException e) {
            retString = "User signup data is not valid, try again with valid information";
        } catch (EmailIdAlreadyRegistered e) {
            retString = "Email Id already registered, please login instead";
        }
        return retString;
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logInUser(UserLoginData userLoginData) {
        String emailId = userLoginData.getEmailId();
        String password = userLoginData.getPassword();
        String response;
        String errorContext;
        try {
            response = userService.logInUser(emailId, password);
            return Response.status(Response.Status.ACCEPTED).entity(response).build();
        } catch (InvalidInputException e) {
            errorContext = "User login data is null, try again with valid information";
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(errorContext).build();
        } catch (EmailIdNotRegistered e) {
            errorContext = "Email Id not registered, please signup instead";
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(errorContext).build();
        } catch (IncorrectPassword e) {
            errorContext = "incorrect password, please provide valid password";
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(errorContext).build();
        }
    }

}
