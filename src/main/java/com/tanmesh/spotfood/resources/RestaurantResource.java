package com.tanmesh.spotfood.resources;

import com.google.common.base.Preconditions;
import com.tanmesh.spotfood.authentication.UserSession;
import com.tanmesh.spotfood.service.IRestaurantService;
import com.tanmesh.spotfood.wsRequestModel.RestaurantData;
import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/restaurant")
public class RestaurantResource {
    private IRestaurantService restaurantService;

    public RestaurantResource(IRestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @POST
    @Path("getNearby")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTag(@Auth UserSession userSession, RestaurantData restaurantData) {
        Preconditions.checkNotNull(restaurantData.getAddress(), "Address should not be null");

        List<String> nearbyRestaurant;
        try {
            nearbyRestaurant = restaurantService.nearbyRestaurant(restaurantData.getAddress());
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

        return Response.status(Response.Status.ACCEPTED).entity(nearbyRestaurant).build();
    }
}
