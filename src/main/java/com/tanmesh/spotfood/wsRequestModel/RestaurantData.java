package com.tanmesh.spotfood.wsRequestModel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RestaurantData {
    private String id;
    private String name;
    private List<String> imgUrls;
    private String address;
    private String phoneNumber;
    private String url;
    private Double latitude;
    private Double longitude;
    private List<String> nearbyRestaurant;
    private Double rating;
}
