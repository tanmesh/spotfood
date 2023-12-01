package com.tanmesh.spotfood.wsRequestModel;

import com.tanmesh.spotfood.enums.SearchOn;
import com.tanmesh.spotfood.enums.SearchType;

import java.util.List;

/**
 * Created by tanmesh
 * Date: 2019-09-09
 * Time: 12:45
 */
public class SearchData {
    private SearchOn searchOn;
    private SearchType type;
    private List<String> tag;
    private int radius = -1;
    private Double longitude;
    private Double latitude;

    public SearchData() {
    }

    public SearchOn getSearchOn() {
        return searchOn;
    }

    public void setSearchOn(SearchOn searchOn) {
        this.searchOn = searchOn;
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

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
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
