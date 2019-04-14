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

@Path("/user_auth")
public class UserAuthResource {
    private IUserService userService;

    public UserAuthResource(IUserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public String signUpUser(UserData userData) throws ApiException  {
        String retString;
        try {
            if (userService.signUpUser(userData)) {
                retString = "SUCCESS";
            } else {
                retString = "FAILED";
            }
        } catch (InvalidInputException e) {
            retString = "User signup data is not valid, try again with valid information";
            //throw new ApiException(Response.Status.EXPECTATION_FAILED,retString);
        } catch (EmailIdAlreadyRegistered e) {
            retString = "Email Id already registered, please login instead";
            //throw new ApiException(Response.Status.BAD_REQUEST, retString);
        }
        return retString;
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public String logInUser(UserLoginData userLoginData) throws ApiException {
        String emailId = userLoginData.getEmailId();
        String password = userLoginData.getPassword();
        String retString;
        try {
            retString = userService.logInUser(emailId, password);
        } catch (InvalidInputException e) {
            retString = "User login data is null, try again with valid information";
            //throw new ApiException(Response.Status.EXPECTATION_FAILED, retString);
        } catch (EmailIdNotRegistered e) {
            retString = "Email Id not registered, please signup instead";
            //throw new ApiException(Response.Status.BAD_REQUEST, retString);
        } catch (IncorrectPassword e) {
            retString = "incorrect password, please provide valid password";
            //throw new ApiException(Response.Status.UNAUTHORIZED, retString);
        }
        return retString;
    }

}
