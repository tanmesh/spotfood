package com.tanmesh.spotfood.resources;

import com.google.common.base.Preconditions;
import com.tanmesh.spotfood.authentication.UserSession;
import com.tanmesh.spotfood.service.IRestaurantService;
import com.tanmesh.spotfood.wsRequestModel.RestaurantData;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/restaurant")
public class RestaurantResource {
    private IRestaurantService restaurantService;

    public RestaurantResource(IRestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

//    @POST
//    @Path("add")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response addTag(@Auth UserSession userSession, RestaurantData restaurantData) {
//        Preconditions.checkNotNull(restaurantData., "Address should not be null");
//
//        List<RestaurantData> nearbyRestaurant;
//        try {
//            nearbyRestaurant = restaurantService.nearbyRestaurant(restaurantData.getAddress());
//        } catch (Exception e) {
//            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
//        }
//
//        return Response.status(Response.Status.ACCEPTED).entity(nearbyRestaurant).build();
//    }

    @GET
    @Path("get/{restaurantId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestaurants(@Auth UserSession userSession, @PathParam("restaurantId") String restaurantId) {
        RestaurantData restaurant;
        try {
            restaurant = restaurantService.getRestaurant(restaurantId);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

        return Response.status(Response.Status.ACCEPTED).entity(restaurant).build();
    }

    @POST
    @Path("getNearby")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNearbyRestaurants(@Auth UserSession userSession, RestaurantData restaurantData) {
        Preconditions.checkNotNull(restaurantData.getAddress(), "Address should not be null");

        List<RestaurantData> nearbyRestaurant;
        try {
            nearbyRestaurant = restaurantService.nearbyRestaurant(restaurantData.getAddress());
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

        return Response.status(Response.Status.ACCEPTED).entity(nearbyRestaurant).build();
    }
}
