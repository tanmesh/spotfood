package com.tanmesh.spotfood.service;

import com.tanmesh.spotfood.dao.RestaurantDAO;
import com.tanmesh.spotfood.entity.Restaurant;
import com.tanmesh.spotfood.utils.GoogleMapAPIs;
import com.tanmesh.spotfood.utils.YelpAPIs;
import com.tanmesh.spotfood.wsRequestModel.RestaurantData;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RestaurantService implements IRestaurantService {
    private Properties props;
    private RestaurantDAO restaurantDAO;

    public RestaurantService(Properties props, RestaurantDAO restaurantDAO) {
        this.props = props;
        this.restaurantDAO = restaurantDAO;
    }

    @Override
    public RestaurantData addRestaurant(String restaurantId) throws Exception {
        Restaurant restaurant = YelpAPIs.getRestaurant(props, restaurantId);

        restaurantDAO.save(restaurant);

        RestaurantData restaurantData = new RestaurantData();
        restaurantData.setId(restaurantId);
        restaurantData.setName(restaurant.getName());
        restaurantData.setAddress(restaurant.getAddress());
        restaurantData.setPhoneNumber(restaurant.getPhoneNumber());
        restaurantData.setUrl(restaurant.getUrl());
        restaurantData.setImgUrls(restaurant.getImgUrls());
        return restaurantData;
    }

    @Override
    public List<RestaurantData> nearbyRestaurant(String address) {
        double[] coordinates = GoogleMapAPIs.getLatLong(props, address);

        List<RestaurantData> restaurantData = new ArrayList<>();
        try {
            List<Restaurant> restaurants = YelpAPIs.getNearbyRestaurant(props, coordinates);

            for (Restaurant restaurant : restaurants) {
                RestaurantData data = new RestaurantData();
                data.setId(restaurant.getId().toString());
                data.setName(restaurant.getName());
                restaurantData.add(data);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return restaurantData;
    }

    @Override
    public RestaurantData getRestaurant(String restaurantId) {
        Restaurant restaurant = restaurantDAO.get(restaurantId);

        RestaurantData restaurantData = new RestaurantData();
        restaurantData.setId(restaurantId);
        restaurantData.setName(restaurant.getName());
        restaurantData.setAddress(restaurant.getAddress());
        restaurantData.setPhoneNumber(restaurant.getPhoneNumber());
        restaurantData.setUrl(restaurant.getUrl());
        restaurantData.setLatitude(restaurant.getLatLong().getCoordinates()[0]);
        restaurantData.setLongitude(restaurant.getLatLong().getCoordinates()[1]);
        restaurantData.setRating(restaurant.getRating());
        restaurantData.setImgUrls(restaurant.getImgUrls());
        return restaurantData;
    }
}
