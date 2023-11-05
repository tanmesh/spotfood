package com.tanmesh.splatter.wsRequestModel;

import com.tanmesh.splatter.enums.SearchType;

/**
 * Created by tanmesh
 * Date: 2019-09-09
 * Time: 12:45
 */
public class SearchData {
    private String name;
    private SearchType type;

    private int radius;

    public SearchData() {
    }

    public SearchData(String name, SearchType type, int radius) {
        this.name = name;
        this.type = type;
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SearchType getType() {
        return type;
    }

    public void setType(SearchType type) {
        this.type = type;
    }
}
