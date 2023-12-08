package com.tanmesh.spotfood.service;

import com.tanmesh.spotfood.wsRequestModel.RestaurantData;

import java.util.List;

public interface IRestaurantService {
    RestaurantData addRestaurant(String restaurantId) throws Exception;

    List<RestaurantData> nearbyRestaurant(String address);

    RestaurantData getRestaurant(String restaurantId);
}
