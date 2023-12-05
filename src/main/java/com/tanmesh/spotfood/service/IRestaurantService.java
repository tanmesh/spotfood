package com.tanmesh.spotfood.service;

import java.util.List;

public interface IRestaurantService {
    List<String> nearbyRestaurant(String address);

}
