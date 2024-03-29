package com.tanmesh.spotfood.entity;

/**
 * Created by tanmesh
 * Date: 2019-09-09
 * Time: 14:20
 */
public class LatLong {
    private String type;
    private double[] coordinates;

    public LatLong() {
    }

    public LatLong(double[] coordinates) {
        this.type = "Point";
        this.coordinates = coordinates;
    }

    public LatLong(double lat, double lng) {
        this.type = "Point";

        double[] coordinates = new double[2];
        coordinates[0] = lat;
        coordinates[1] = lng;
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
