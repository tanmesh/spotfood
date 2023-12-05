package com.tanmesh.spotfood.service;

import com.tanmesh.spotfood.utils.GoogleMapAPIs;
import com.tanmesh.spotfood.utils.YelpAPIs;

import java.util.List;
import java.util.Properties;

public class RestaurantService implements IRestaurantService {
    private Properties props;

    public RestaurantService(Properties props) {
        this.props = props;
    }

    @Override
    public List<String> nearbyRestaurant(String address) {
        double[] coordinates = GoogleMapAPIs.getLatLong(props, address);

        try {
            return YelpAPIs.getNearbyRestaurant(props, coordinates);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
