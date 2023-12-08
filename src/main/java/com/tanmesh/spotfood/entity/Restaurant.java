package com.tanmesh.spotfood.entity;

import lombok.Getter;
import lombok.Setter;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

@Entity(value = "restaurant", noClassnameStored = true)
@Getter
@Setter
public class Restaurant {
    @Id
    private String id;
    private String name;
    private List<String> imgUrls;
    private String address;
    private LatLong latLong;
    private String phoneNumber;
    private String url;
    private double rating;
}
