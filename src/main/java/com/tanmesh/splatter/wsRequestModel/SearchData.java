package com.tanmesh.splatter.wsRequestModel;

import com.tanmesh.splatter.enums.SearchType;

/**
 * Created by tanmesh
 * Date: 2019-09-09
 * Time: 12:45
 */
public class SearchData {
    private SearchType type;
    private String tag;
    private int radius;
    private Double longitude;
    private Double latitude;

    public SearchData() {
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public SearchType getType() {
        return type;
    }

    public void setType(SearchType type) {
        this.type = type;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
