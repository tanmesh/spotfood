package com.tanmesh.splatter.entity;

import java.util.List;

/**
 * Created by tanmesh
 * Date: 2019-09-09
 * Time: 14:20
 */
public class LatLong {
    private String type;
    private List<Double> coordinates;

    public LatLong(List<Double> coordinates) {
        this.type = "Point";
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
