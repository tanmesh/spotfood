package com.tanmesh.spotfood.wsRequestModel;

import java.util.List;

public class RestaurantData {
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;

    private List<String> nearbyRestaurant;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<String> getNearbyRestaurant() {
        return nearbyRestaurant;
    }

    public void setNearbyRestaurant(List<String> nearbyRestaurant) {
        this.nearbyRestaurant = nearbyRestaurant;
    }
}
